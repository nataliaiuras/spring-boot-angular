package com.example.models.dtos.transaction;

import com.example.utils.enums.TransactionStatus;
import com.example.utils.enums.TransactionType;

import java.math.BigDecimal;

public record TransactionOverviewDto(Long id, BigDecimal amount, TransactionType type, TransactionStatus status,
                                     String transactionDate, String referenceNumber) {
}