package com.example.services.impl;

import com.example.exceptions.domain.account.AccountInactiveException;
import com.example.exceptions.domain.account.AccountNotFoundException;
import com.example.exceptions.domain.card.CardNotFoundException;
import com.example.exceptions.domain.transaction.DailyTransferLimitExceededException;
import com.example.exceptions.domain.transaction.InsufficientBalanceException;
import com.example.exceptions.domain.transaction.TransactionNotFoundException;
import com.example.models.dtos.transaction.*;
import com.example.models.entities.Account;
import com.example.models.entities.Card;
import com.example.models.entities.Transaction;
import com.example.repository.AccountRepository;
import com.example.repository.CardRepository;
import com.example.repository.TransactionRepository;
import com.example.services.CurrencyExchangeService;
import com.example.services.TransactionService;
import com.example.utils.enums.CurrencyType;
import com.example.utils.enums.OperationType;
import com.example.utils.enums.TransactionStatus;
import com.example.utils.mapers.TransactionMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class TransactionServiceImpl implements TransactionService {


    private static final BigDecimal DAYLY_TRANSFER_LIMIT = new BigDecimal("5000");

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final CurrencyExchangeService currencyExchangeService;


    @Override
    public Page<TransactionOverviewDto> getAllTransactions(Pageable pageable) {
        Page<Transaction> transactionPage = transactionRepository.findAll(pageable);
        return transactionPage.map(transactionMapper::toTransactionOverviewDto);
    }

    @Override
    public TransactionOverviewDto getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
        return transactionMapper.toTransactionOverviewDto(transaction);
    }

    @Override
    public Page<TransactionOverviewDto> getTransactionsByAccount(Long accountId, LocalDate startDate, LocalDate endDate, String type, String status, Pageable pageable) {
        Instant startDateInstant = null;
        Instant endDateInstant = null;

        if (startDate != null) {
            startDateInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        }
        if (endDate != null && startDate != null && startDate.isAfter(endDate)) {
            endDateInstant = startDate.atTime(23, 59, 59, 999_999_999).atZone(ZoneId.systemDefault()).toInstant();
        }

        Page<Transaction> transactionPage = transactionRepository.findTransactionsByAccount(accountId, startDateInstant, endDateInstant, type, status, pageable);
        return transactionPage.map(transactionMapper::toTransactionOverviewDto);

    }

    @Override
    public TransactionDto deposit(Long accountId, BigDecimal amount, String description, CurrencyType currencyType) {
        validateTransactionAmount(amount);
        Account account = getAccountById(accountId);
        BigDecimal fee = getFee(amount, OperationType.DEPOSIT);

        return processAccountTransaction(account, amount, description, OperationType.DEPOSIT,
                acc -> acc.setBalance(acc.getBalance().add(amount.add(fee))));
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

    @Override
    public TransactionDto withdraw(Long accountId, BigDecimal amount, String description, CurrencyType currencyType) {
        validateTransactionAmount(amount);
        Account account = getAccountById(accountId);

        BigDecimal fee = getFee(amount, OperationType.WITHDRAWAL);
        BigDecimal totalAmount = amount.add(fee);
        validateWithdrawal(account, totalAmount);

        return processAccountTransaction(account, totalAmount, description, OperationType.WITHDRAWAL,
                acc -> acc.setBalance(acc.getBalance().subtract(totalAmount)));
    }

    @Override
    public TransactionDto cancelTransaction(Long id) {
        return null;
    }

    @Override
    public TransactionDto reverseTransaction(Long id, String reason) {
        return null;
    }

    @Override
    public TransactionDto createRecurringTransaction(RecurringTransactionDto recurringTransactionDto) {
        return null;
    }

    @Override
    public Page<RecurringTransactionDto> getRecurringTransactions(Long accountId, Pageable pageable) {
        return null;
    }

    @Override
    public TransactionSummaryDto getTransactionSummary(Long accountId, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public List<TransactionCategoryDto> getTransactionsByCategory(Long accountId, LocalDate startDate, LocalDate endDate) {
        return List.of();
    }

    @Override
    public Page<TransactionOverviewDto> getTransactionsPeriodByAccount(Long accountId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Instant startDateInstant = null;
        Instant endDateInstant = null;
        if (startDate != null) {
            startDateInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        }
        if (endDate != null) {
            endDateInstant = endDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        }
        Page<Transaction> transactionPage = transactionRepository.findTransactionsPeriodByAccount(accountId, startDateInstant, endDateInstant, pageable);
        return transactionPage.map(transactionMapper::toTransactionOverviewDto);
    }

    @Override
    public TransferResponseDto processTransfer(TransferRequestDto transferRequest) {
        validateTransferRequest(transferRequest);

        Account sourceAccount = accountRepository.findById(transferRequest.getSourceAccountId())
                .orElseThrow(() -> new AccountNotFoundException(transferRequest.getSourceAccountId()));
        Account destinationAccount = findDestinationAccount(transferRequest.getRecipientIdentifier());

        return executeTransfer(transferRequest, sourceAccount, destinationAccount);
    }

    private void validateTransactionAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }
    }

    private Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    private void validateWithdrawal(Account account, BigDecimal amount) {
        if (!account.isActive()) {
            throw new AccountInactiveException(account.getId());
        }
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(account.getId(), amount);
        }
    }

    private TransactionDto processAccountTransaction(Account account, BigDecimal amount, String description,
                                                     OperationType operationType, Consumer<Account> balanceUpdater) {
        balanceUpdater.accept(account);
        accountRepository.save(account);

        Transaction transaction = createAccountTransaction(account, amount, description, operationType);
        Transaction savedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toTransactionDto(savedTransaction);
    }

    private Transaction createAccountTransaction(Account account, BigDecimal amount, String description, OperationType operationType) {
        Instant now = Instant.now();

        return Transaction.builder()
                .sourceAmount(amount)
                .operationType(operationType)
                .status(TransactionStatus.COMPLETED)
                .transactionDate(now)
                //.sourceAccount(account)
                .destinationAccount(account)
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

    private TransferResponseDto executeTransfer(TransferRequestDto transferRequest, Account sourceAccount, Account destinationAccount) {
        validateAccountsForTransfer(sourceAccount, destinationAccount, transferRequest.getAmount());

        CurrencyType sourceCurrency = sourceAccount.getCurrency();
        CurrencyType destinationCurrency = destinationAccount.getCurrency();

        BigDecimal sourceAmount = transferRequest.getAmount();
        BigDecimal conversionFee = currencyExchangeService.calculateConversionFee(
                sourceAmount, sourceCurrency, destinationCurrency);
        BigDecimal destinationAmount = currencyExchangeService.convertAmount(
                sourceAmount, sourceCurrency, destinationCurrency);

        BigDecimal fee = getFee(transferRequest.getAmount(), transferRequest.getOperationType());

        BigDecimal totalDeduction = sourceAmount.add(conversionFee).add(fee);

        validateAccountsForTransfer(sourceAccount, destinationAccount, totalDeduction);

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(totalDeduction));

        destinationAccount.setBalance(destinationAccount.getBalance().add(destinationAmount));

        accountRepository.saveAll(List.of(sourceAccount, destinationAccount));

        Transaction transaction = createCurrencyAwareTransaction(
                transferRequest, sourceAccount, destinationAccount,
                sourceAmount, destinationAmount, conversionFee, fee, sourceCurrency, destinationCurrency);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return TransferResponseDto
                .builder()
                .referenceNumber(savedTransaction.getReferenceNumber())
                .status(savedTransaction.getStatus())
                .sourceAmount(sourceAmount)
                .convertedAmount(destinationAmount)
                .conversionRate(currencyExchangeService.getExchangeRate(sourceCurrency, destinationCurrency))
                .conversionFee(conversionFee)
                .sourceCurrency(sourceCurrency)
                .destinationCurrency(destinationCurrency)
                .operationFee(fee)
                .destinationAmount(destinationAmount)
                .transactionDate(savedTransaction.getCreatedDate())
                .build();

    }

    private Transaction createCurrencyAwareTransaction(TransferRequestDto transferRequest,
                                                       Account fromAccount, Account toAccount,
                                                       BigDecimal sourceAmount, BigDecimal destinationAmount,
                                                       BigDecimal conversionFee, BigDecimal fee, CurrencyType sourceCurrency,
                                                       CurrencyType destinationCurrency) {
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
                .description(buildCurrencyDescription(transferRequest.getDescription(),
                        sourceAmount, destinationAmount, sourceCurrency, destinationCurrency, conversionFee))
                .status(TransactionStatus.COMPLETED)
                .transactionDate(Instant.now())
                .build();
    }

    private String buildCurrencyDescription(String originalDescription, BigDecimal sourceAmount,
                                            BigDecimal destinationAmount, CurrencyType sourceCurrency,
                                            CurrencyType destinationCurrency, BigDecimal conversionFee) {
        StringBuilder description = new StringBuilder();
        if (originalDescription != null) {
            description.append(originalDescription).append(" | ");
        }

        if (!sourceCurrency.equals(destinationCurrency)) {
            description.append(String.format("Currency conversion: %s %s → %s %s",
                    sourceAmount, sourceCurrency, destinationAmount, destinationCurrency));
            if (conversionFee.compareTo(BigDecimal.ZERO) > 0) {
                description.append(String.format(" | Conversion fee: %s %s", conversionFee, sourceCurrency));
            }
        }

        return description.toString();
    }

    private Transaction createTransaction(TransferRequestDto transferRequest, Account fromAccount, Account toAccount) {
        return Transaction
                .builder()
                .sourceAmount(transferRequest.getAmount())
                .operationType(transferRequest.getOperationType())
                .status(TransactionStatus.COMPLETED)
                .sourceAccount(fromAccount)
                .destinationAccount(toAccount)
                .referenceNumber(generateReferenceNumber())
                .description(transferRequest.getDescription())
                .transactionDate(Instant.now())
                .build();
    }

    private void validateTransferRequest(TransferRequestDto transferRequest) {
        if (transferRequest == null) {
            throw new IllegalArgumentException("Transfer request cannot be null");
        }
        if (transferRequest.getSourceAccountId() == null) {
            throw new IllegalArgumentException("From account ID cannot be null");
        }
        if (transferRequest.getOperationType() == null) {
            throw new IllegalArgumentException("Operation type cannot be null");
        }
    /*    if (transferRequest.getTransferType() == TransferType.WITHDRAWAL && transferRequest.getAmount() == null) {}
        if (transferRequest.getRecipientIdentifier() == null) {
            throw new IllegalArgumentException("Destination identifier cannot be null");
        }*/


        //TODO check if the transfer don't go to the same account
        /*if (transferRequest.getFromAccountId().equals(transferRequest.getDestinationIdentifier())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }*/
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
