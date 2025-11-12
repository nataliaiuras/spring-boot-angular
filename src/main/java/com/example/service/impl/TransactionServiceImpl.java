package com.example.service.impl;

import com.example.dto.request.transaction.TransactionCreateRecurringRequest;
import com.example.dto.response.transaction.*;
import com.example.entity.Transaction;
import com.example.exception.domain.transaction.TransactionNotFoundException;
import com.example.mapper.TransactionMapper;
import com.example.repository.TransactionRepository;

import com.example.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    @Override
    public Page<TransactionResponse> getAllTransactions(Pageable pageable) {
        Page<Transaction> transactionPage = transactionRepository.findAll(pageable);
        return transactionPage.map(transactionMapper::toTransactionResponse);
    }

    @Override
    public TransactionResponse getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
        return transactionMapper.toTransactionResponse(transaction);
    }

    @Override
    public Page<TransactionResponse> getTransactionsByAccount(Long accountId, LocalDate startDate, LocalDate endDate, String type, String status, Pageable pageable) {
        Instant startDateInstant = null;
        Instant endDateInstant = null;

        if (startDate != null) {
            startDateInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        }
        if (endDate != null && startDate != null && startDate.isAfter(endDate)) {
            endDateInstant = startDate.atTime(23, 59, 59, 999_999_999).atZone(ZoneId.systemDefault()).toInstant();
        }

        Page<Transaction> transactionPage = transactionRepository.findTransactionsByAccount(accountId, startDateInstant, endDateInstant, type, status, pageable);
        return transactionPage.map(transactionMapper::toTransactionResponse);

    }

    @Override
    public TransactionDetailResponse cancelTransaction(Long id) {
        return null;
    }

    @Override
    public TransactionDetailResponse reverseTransaction(Long id, String reason) {
        return null;
    }

    @Override
    public RecurringTransactionResponse createRecurringTransaction(TransactionCreateRecurringRequest request) {
        return null;
    }

    @Override
    public Page<RecurringTransactionResponse> getRecurringTransactions(Long accountId, Pageable pageable) {
        return null;
    }

    @Override
    public TransactionSummaryResponse getTransactionSummary(Long accountId, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public List<TransactionCategoryResponse> getTransactionsByCategory(Long accountId, LocalDate startDate, LocalDate endDate) {
        return List.of();
    }

    @Override
    public Page<TransactionResponse> getTransactionsPeriodByAccount(Long accountId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Instant startDateInstant = null;
        Instant endDateInstant = null;
        if (startDate != null) {
            startDateInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        }
        if (endDate != null) {
            endDateInstant = endDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        }
        Page<Transaction> transactionPage = transactionRepository.findTransactionsPeriodByAccount(accountId, startDateInstant, endDateInstant, pageable);
        return transactionPage.map(transactionMapper::toTransactionResponse);
    }


}
