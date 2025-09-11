package com.example.dtos.transaction;

import com.example.utils.TransactionStatus;
import com.example.utils.TransactionType;

import java.math.BigDecimal;

public record TransactionOverviewDto(Long id, BigDecimal amount, TransactionType type, TransactionStatus status,
                                     String transactionDate, String referenceNumber) {
}