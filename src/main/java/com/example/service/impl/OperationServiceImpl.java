package com.example.service.impl;

import com.example.dto.request.transaction.TransferRequest;
import com.example.dto.response.transaction.TransactionResponse;
import com.example.dto.response.transaction.TransferResponse;
import com.example.entity.Account;
import com.example.entity.Card;
import com.example.entity.Transaction;
import com.example.mapper.TransactionMapper;
import com.example.repository.AccountRepository;
import com.example.repository.CardRepository;
import com.example.repository.TransactionRepository;
import com.example.service.CurrencyExchangeService;
import com.example.service.OperationService;
import com.example.util.enums.CurrencyType;
import com.example.util.enums.OperationType;
import com.example.util.enums.TransactionStatus;
import com.example.exception.domain.account.AccountInactiveException;
import com.example.exception.domain.account.AccountNotFoundException;
import com.example.exception.domain.card.CardNotFoundException;
import com.example.exception.domain.transaction.DailyTransferLimitExceededException;
import com.example.exception.domain.transaction.InsufficientBalanceException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.function.Consumer;


@Service
@Transactional
@AllArgsConstructor
public class OperationServiceImpl implements OperationService {

    private static final BigDecimal DAYLY_TRANSFER_LIMIT = new BigDecimal("5000");

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final CurrencyExchangeService currencyExchangeService;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    @Override
    public TransactionResponse deposit(Long accountId, BigDecimal amount, String description, CurrencyType currencyType) {
        validateAmount(amount);
        Account account = getAccountById(accountId);
        BigDecimal fee = getFee(amount, OperationType.DEPOSIT);

        return processAccountOperation(account, amount, description, OperationType.DEPOSIT, acc -> acc.setBalance(acc.getBalance().add(amount.add(fee))));
    }

    @Override
    public TransactionResponse withdraw(Long accountId, BigDecimal amount, String description, CurrencyType currencyType) {

        Account account = getAccountById(accountId);
        if (account.getCurrency() != currencyType) {

        }
        validateAmount(amount);
        BigDecimal fee = getFee(amount, OperationType.WITHDRAWAL);
        BigDecimal totalAmount = amount.add(fee);
        validateWithdrawal(account, totalAmount);

        return processAccountOperation(account, totalAmount, description, OperationType.WITHDRAWAL, acc -> acc.setBalance(acc.getBalance().subtract(totalAmount)));
    }

    @Override
    public TransferResponse processTransfer(TransferRequest transferRequest) {
        validateTransferRequest(transferRequest);

        Account sourceAccount = getAccountById(transferRequest.getSourceAccountId());
        Account destinationAccount = findDestinationAccount(transferRequest.getRecipientIdentifier());
        if (sourceAccount.equals(destinationAccount)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        return executeTransfer(transferRequest, sourceAccount, destinationAccount);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    private Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    private BigDecimal getFee(BigDecimal amount, OperationType operationType) {
        return switch (operationType) {
            case DEPOSIT -> amount.multiply(BigDecimal.valueOf(0.01));
            case WITHDRAWAL -> amount.multiply(BigDecimal.valueOf(0.02));
            case TRANSFER_INTERNAL -> amount.multiply(BigDecimal.valueOf(0.01).add(BigDecimal.valueOf(0.005)));
            case TRANSFER_EXTERNAL -> amount.multiply(BigDecimal.valueOf(0.02).add(BigDecimal.valueOf(0.005)));
            default -> throw new IllegalArgumentException("Unsupported operation type: " + operationType);
        };
    }

    private void validateWithdrawal(Account account, BigDecimal amount) {
        if (!account.isActive()) {
            throw new AccountInactiveException(account.getId());
        }
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(account.getId(), amount);
        }
    }

    private TransactionResponse processAccountOperation(Account account, BigDecimal amount, String description, OperationType operationType, Consumer<Account> balanceUpdater) {
        balanceUpdater.accept(account);
        accountRepository.save(account);

        Transaction transaction = buildTransaction(account, amount, description, operationType);
        Transaction savedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toTransactionResponse(savedTransaction);
    }

    private Transaction buildTransaction(Account account, BigDecimal amount, String description, OperationType operationType) {
        if (operationType == OperationType.DEPOSIT) {
            return Transaction.builder()
                    .operationType(operationType)
                    .sourceAccount(null)
                    .destinationAccount(account)
                    .sourceAmount(amount)
                    .sourceCurrency(account.getCurrency())
                    .destinationCurrency(account.getCurrency())
                    .conversionRate(currencyExchangeService.getExchangeRate(account.getCurrency(), account.getCurrency()))
                    .conversionFee(BigDecimal.ZERO)
                    .convertedAmount(amount)
                    .operationFee(BigDecimal.ZERO)
                    .destinationAmount(amount)
                    .referenceNumber(generateReferenceNumber())
                    .description(description)
                    .status(TransactionStatus.COMPLETED)
                    .transactionDate(Instant.now())
                    .build();
        }
        else
            return Transaction.builder()
                    .operationType(operationType)
                    .sourceAccount(account)
                    .destinationAccount(account)
                    .sourceAmount(amount)
                    .sourceCurrency(account.getCurrency())
                    .destinationCurrency(account.getCurrency())
                    .status(TransactionStatus.COMPLETED)
                    .transactionDate(Instant.now())
                    .referenceNumber(generateReferenceNumber())
                    .description(description)
                    .build();

    }

    private Account findDestinationAccount(String destinationIdentifier) {
        if (destinationIdentifier == null) {
            throw new IllegalArgumentException("Destination identifier cannot be null");
        }

        return switch (destinationIdentifier.length()) {
            case 12 -> accountRepository.findByAccountNumber(destinationIdentifier)
                    .orElseThrow(() -> new AccountNotFoundException(destinationIdentifier));
            case 16 -> accountRepository.findByIbanCode(destinationIdentifier)
                    .orElseThrow(() -> new AccountNotFoundException(destinationIdentifier));
            case 24 -> {
                Card card = cardRepository.findByCardNumber(destinationIdentifier)
                        .orElseThrow(() -> new CardNotFoundException(destinationIdentifier));
                yield card.getAccount();
            }
            default ->
                    throw new IllegalArgumentException("Invalid destination identifier length: " + destinationIdentifier.length());
        };
    }

    private TransferResponse executeTransfer(TransferRequest transferRequest, Account sourceAccount, Account destinationAccount) {
        validateAccountsForTransfer(sourceAccount, destinationAccount, transferRequest.getAmount());

        CurrencyType sourceCurrency = sourceAccount.getCurrency();
        CurrencyType destinationCurrency = destinationAccount.getCurrency();

        BigDecimal sourceAmount = transferRequest.getAmount();
        BigDecimal conversionFee = currencyExchangeService.calculateConversionFee(sourceAmount, sourceCurrency, destinationCurrency);
        BigDecimal destinationAmount = currencyExchangeService.convertAmount(sourceAmount, sourceCurrency, destinationCurrency);

        BigDecimal fee = getFee(transferRequest.getAmount(), transferRequest.getOperationType());

        BigDecimal totalDeduction = sourceAmount.add(conversionFee).add(fee);

        validateAccountsForTransfer(sourceAccount, destinationAccount, totalDeduction);

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(totalDeduction));

        destinationAccount.setBalance(destinationAccount.getBalance().add(destinationAmount));

        accountRepository.saveAll(List.of(sourceAccount, destinationAccount));

        Transaction transaction = createCurrencyAwareTransaction(transferRequest, sourceAccount, destinationAccount, sourceAmount, destinationAmount, conversionFee, fee, sourceCurrency, destinationCurrency);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return TransferResponse.builder().referenceNumber(savedTransaction.getReferenceNumber()).status(savedTransaction.getStatus()).sourceAmount(sourceAmount).convertedAmount(destinationAmount).conversionRate(currencyExchangeService.getExchangeRate(sourceCurrency, destinationCurrency)).conversionFee(conversionFee).sourceCurrency(sourceCurrency).destinationCurrency(destinationCurrency).operationFee(fee).destinationAmount(destinationAmount).transactionDate(savedTransaction.getCreatedDate()).build();

    }

    private Transaction createCurrencyAwareTransaction(TransferRequest transferRequest, Account fromAccount, Account toAccount, BigDecimal sourceAmount, BigDecimal destinationAmount, BigDecimal conversionFee, BigDecimal fee, CurrencyType sourceCurrency, CurrencyType destinationCurrency) {
        return Transaction.builder()
                .operationType(transferRequest.getOperationType())
                .sourceAccount(fromAccount)
                .destinationAccount(toAccount)
                .sourceAmount(sourceAmount)
                .sourceCurrency(sourceCurrency)
                .destinationCurrency(destinationCurrency)
                .conversionRate(currencyExchangeService.getExchangeRate(sourceCurrency, destinationCurrency))
                .conversionFee(conversionFee)
                .convertedAmount(destinationAmount)
                .operationFee(fee)
                .destinationAmount(destinationAmount)
                .referenceNumber(generateReferenceNumber())
                .description(buildCurrencyDescription(transferRequest.getDescription(), sourceAmount, destinationAmount, sourceCurrency, destinationCurrency, conversionFee)).status(TransactionStatus.COMPLETED).transactionDate(Instant.now()).build();
    }

    private String buildCurrencyDescription(String originalDescription, BigDecimal sourceAmount, BigDecimal destinationAmount, CurrencyType sourceCurrency, CurrencyType destinationCurrency, BigDecimal conversionFee) {
        StringBuilder description = new StringBuilder();
        if (originalDescription != null) {
            description.append(originalDescription).append(" | ");
        }

        if (!sourceCurrency.equals(destinationCurrency)) {
            description.append(String.format("Currency conversion: %s %s → %s %s", sourceAmount, sourceCurrency, destinationAmount, destinationCurrency));
            if (conversionFee.compareTo(BigDecimal.ZERO) > 0) {
                description.append(String.format(" | Conversion fee: %s %s", conversionFee, sourceCurrency));
            }
        }

        return description.toString();
    }

    private Transaction createTransaction(TransferRequest transferRequest, Account fromAccount, Account toAccount) {
        return Transaction.builder().sourceAmount(transferRequest.getAmount()).operationType(transferRequest.getOperationType()).status(TransactionStatus.COMPLETED).sourceAccount(fromAccount).destinationAccount(toAccount).referenceNumber(generateReferenceNumber()).description(transferRequest.getDescription()).transactionDate(Instant.now()).build();
    }

    private void validateTransferRequest(TransferRequest transferRequest) {
        if (transferRequest == null) {
            throw new IllegalArgumentException("Transfer request cannot be null");
        }
        if (transferRequest.getSourceAccountId() == null) {
            throw new IllegalArgumentException("Source account ID cannot be null");
        }
        if (transferRequest.getOperationType() == null) {
            throw new IllegalArgumentException("Operation type cannot be null");
        }
        if (transferRequest.getAmount() == null || transferRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

    }

    private void validateAccountsForTransfer(Account sourceAccount, Account destinationAccount, BigDecimal amount) {
        if (!sourceAccount.isActive()) {
            throw new AccountInactiveException(sourceAccount.getId());
        }
        if (!destinationAccount.isActive()) {
            throw new AccountInactiveException(destinationAccount.getId());
        }
        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(sourceAccount.getId(), amount);
        }
        BigDecimal todayTransfers = getTodayTransferAmount(sourceAccount.getId());

        if (todayTransfers.add(amount).compareTo(DAYLY_TRANSFER_LIMIT) > 0) {
            throw new DailyTransferLimitExceededException(sourceAccount.getId(), DAYLY_TRANSFER_LIMIT);
        }
    }

    private BigDecimal getTodayTransferAmount(Long accountId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        Instant startOfDayInstant = startOfDay.atZone(ZoneId.systemDefault()).toInstant();
        Instant endOfDayInstant = endOfDay.atZone(ZoneId.systemDefault()).toInstant();

        return transactionRepository.sumTransferAmountByAccountAndDateRange(accountId, startOfDayInstant, endOfDayInstant).orElse(BigDecimal.ZERO);
    }

    private String generateReferenceNumber() {
        return "TXN" + System.currentTimeMillis() + String.format("%04d", (int) (Math.random() * 10000));
    }
}
