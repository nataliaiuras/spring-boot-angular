package com.example.services;

import com.example.models.dtos.transaction.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    Page<TransactionOverviewDto> getAllTransactions(Pageable pageable);

    TransactionOverviewDto getTransactionById(Long id);

    Page<TransactionOverviewDto> getTransactionsByAccount(Long accountId, LocalDate startDate, LocalDate endDate, String type, String status, Pageable pageable);

    TransactionDto transferMoney(TransferRequestDto transferRequest);

    TransactionDto deposit(Long accountId, BigDecimal amount, String description);

    TransactionDto withdraw(Long accountId, BigDecimal amount, String description);

    TransactionDto cancelTransaction(Long id);

    TransactionDto reverseTransaction(Long id, String reason);

    TransactionDto createRecurringTransaction(RecurringTransactionDto recurringTransactionDto);

    Page<RecurringTransactionDto> getRecurringTransactions(Long accountId, Pageable pageable);

    TransactionSummaryDto getTransactionSummary(Long accountId, LocalDate startDate, LocalDate endDate);

    List<TransactionCategoryDto> getTransactionsByCategory(Long accountId, LocalDate startDate, LocalDate endDate);

    Page<TransactionOverviewDto> getTransactionsPeriodByAccount(Long accountId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    TransferResponseDto processTransfer(@Valid TransferRequestDto transferRequest);
}
