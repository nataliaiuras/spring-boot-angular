package com.example.services.impl;

import com.example.exceptions.domain.account.AccountInactiveException;
import com.example.exceptions.domain.account.AccountNotFoundException;
import com.example.exceptions.domain.transaction.DailyTransferLimitExceededException;
import com.example.exceptions.domain.transaction.InsufficientBalanceException;
import com.example.exceptions.domain.transaction.TransactionNotFoundException;
import com.example.models.dtos.transaction.*;
import com.example.models.entities.Account;
import com.example.models.entities.Transaction;
import com.example.repository.AccountRepository;
import com.example.repository.TransactionRepository;
import com.example.services.TransactionService;
import com.example.utils.constants.AppConstants;
import com.example.utils.enums.TransactionStatus;
import com.example.utils.enums.TransactionType;
import com.example.utils.mapers.AccountMapper;
import com.example.utils.mapers.CustomerMapper;
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
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CustomerMapper customerMapper;


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

    public Page<TransactionOverviewDto> getTransactionsByAccount(Long accountId, LocalDate startDate, LocalDate endDate,
                                                                 String type, String status, Pageable pageable) {
        Instant startDateInstant = null;
        Instant endDateInstant = null;

        if (startDate != null) {
            startDateInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        }
        if (endDate != null && startDate != null && startDate.isAfter(endDate)) {
            endDateInstant = startDate.atTime(23, 59, 59, 999_999_999)
                    .atZone(ZoneId.systemDefault())
                    .toInstant();
        }

        Page<Transaction> transactionPage = transactionRepository.findTransactionsByAccount(
                accountId, startDateInstant, endDateInstant, type, status, pageable);
        return transactionPage.map(transactionMapper::toTransactionOverviewDto);

    }

    @Override
    public TransactionDto transferMoney(TransferRequestDto transferRequest) {
        validateTransferRequest(transferRequest);
        Account fromAccount = accountRepository.findById(transferRequest.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException(transferRequest.getFromAccountId()));
        Account toAccount = accountRepository.findById(transferRequest.getDestinationIdentifier())
                .orElseThrow(() -> new AccountNotFoundException(transferRequest.getDestinationIdentifier()));
        validateAccountsForTransfer(fromAccount, toAccount, transferRequest.getAmount());
        fromAccount.setBalance(fromAccount.getBalance().subtract(transferRequest.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(transferRequest.getAmount()));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        Transaction transaction = Transaction.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(transferRequest.getAmount())
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.COMPLETED)
                .description(transferRequest.getDescription())
                .transactionDate(Instant.now())
                .referenceNumber(generateReferenceNumber())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toTransactionDto(savedTransaction);

    }

    private void validateTransferRequest(TransferRequestDto transferRequest) {
        if (transferRequest == null) {
            throw new IllegalArgumentException("Transfer request cannot be null");
        }
        if (transferRequest.getFromAccountId() == null) {
            throw new IllegalArgumentException("From account ID cannot be null");
        }
        if (transferRequest.getDestinationIdentifier() == null) {
            throw new IllegalArgumentException("To account ID cannot be null");
        }
        if (transferRequest.getFromAccountId().equals(transferRequest.getDestinationIdentifier())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        if (transferRequest.getAmount() == null || transferRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
    }

    private void validateAccountsForTransfer(Account fromAccount, Account toAccount, BigDecimal amount) {
        if (!fromAccount.isActive()) {
            throw new AccountInactiveException(fromAccount.getId());
        }
        if (!toAccount.isActive()) {
            throw new AccountInactiveException(toAccount.getId());
        }
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(fromAccount.getId(), amount);
        }
        BigDecimal dailyTransferLimit = AppConstants.DEFAULT_AMOUNT_DAILY_LIMIT;
        if (dailyTransferLimit != null) {
            BigDecimal todayTransfers = getTodayTransferAmount(fromAccount.getId());
            if (todayTransfers.add(amount).compareTo(dailyTransferLimit) > 0) {
                throw new DailyTransferLimitExceededException(fromAccount.getId(), dailyTransferLimit);
            }
        }
    }

    private BigDecimal getTodayTransferAmount(Long accountId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        return transactionRepository.sumTransferAmountByAccountAndDateRange(accountId, startOfDay, endOfDay)
                .orElse(BigDecimal.ZERO);
    }

    private String generateReferenceNumber() {
        return "TXN" + System.currentTimeMillis() +
                String.format("%04d", (int) (Math.random() * 10000));
    }



    @Override
    public TransactionDto deposit(Long accountId, BigDecimal amount, String description) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .type(TransactionType.DEPOSIT)
                .status(TransactionStatus.COMPLETED)
                .transactionDate(Instant.now())
                .fromCustomer(account.getCustomer())
                .fromAccount(account)
                .toCustomer(account.getCustomer()) // Use managed entity
                .toAccount(account)
                .referenceNumber(generateReferenceNumber())
                .description(description)
                .completedDate(Instant.now())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toTransactionDto(savedTransaction);
    }

    @Override
    public TransactionDto withdraw(Long accountId, BigDecimal amount, String description) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        if (!account.isActive() || account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(accountId, amount);
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .type(TransactionType.WITHDRAWAL)
                .status(TransactionStatus.COMPLETED)
                .transactionDate(Instant.now())
                .fromCustomer(account.getCustomer()) // Use managed entity
                .fromAccount(account)
                .toCustomer(account.getCustomer()) // Use managed entity
                .toAccount(account)
                .referenceNumber(generateReferenceNumber())
                .description(description)
                .completedDate(Instant.now())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toTransactionDto(savedTransaction);
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
        Page<Transaction> transactionPage = transactionRepository.findTransactionsPeriodByAccount(
                accountId, startDateInstant, endDateInstant, pageable);
        return transactionPage.map(transactionMapper::toTransactionOverviewDto);
    }

    @Override
    public TransferResponseDto processTransfer(TransferRequestDto transferRequest) {
        validateTransferRequest(transferRequest);
        Account fromAccount = accountRepository.findById(transferRequest.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException(transferRequest.getFromAccountId()));
        Account toAccount = accountRepository.findById(transferRequest.getDestinationIdentifier())
                .orElseThrow(() -> new AccountNotFoundException(transferRequest.getDestinationIdentifier()));
        validateAccountsForTransfer(fromAccount, toAccount, transferRequest.getAmount());
        fromAccount.setBalance(fromAccount.getBalance().subtract(transferRequest.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(transferRequest.getAmount()));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        Transaction transaction = Transaction.builder()
                .amount(transferRequest.getAmount())
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.COMPLETED)
                .fromCustomer(fromAccount.getCustomer())
                .fromAccount(fromAccount)
                .toCustomer(toAccount.getCustomer())
                .toAccount(toAccount)
                .referenceNumber(generateReferenceNumber())
                .description(transferRequest.getDescription())
                .transactionDate(Instant.now())
                .completedDate(Instant.now())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toTransferResponseDto(savedTransaction);
    }

    public Set<TransactionDto> getAllTransactionsByAccountId(Long accountId) {
        return null;
    }
}
