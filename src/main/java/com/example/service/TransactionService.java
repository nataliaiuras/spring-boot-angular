package com.example.service;

import com.example.dto.request.transaction.TransactionCreateRecurringRequest;
import com.example.dto.response.transaction.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    Page<TransactionResponse> getAllTransactions(Pageable pageable);

    TransactionResponse getTransactionById(Long id);

    Page<TransactionResponse> getTransactionsByAccount(Long accountId, LocalDate startDate, LocalDate endDate, String type, String status, Pageable pageable);

    TransactionDetailResponse cancelTransaction(Long id);

    TransactionDetailResponse reverseTransaction(Long id, String reason);

    RecurringTransactionResponse createRecurringTransaction(TransactionCreateRecurringRequest request);

    Page<RecurringTransactionResponse> getRecurringTransactions(Long accountId, Pageable pageable);

    TransactionSummaryResponse getTransactionSummary(Long accountId, LocalDate startDate, LocalDate endDate);

    List<TransactionCategoryResponse> getTransactionsByCategory(Long accountId, LocalDate startDate, LocalDate endDate);

    Page<TransactionResponse> getTransactionsPeriodByAccount(Long accountId, LocalDate startDate, LocalDate endDate, Pageable pageable);


}
