package com.example.models.dtos.transaction;

import com.example.utils.enums.OperationType;
import com.example.utils.enums.TransactionStatus;

import java.math.BigDecimal;

public record TransactionOverviewDto(Long id, BigDecimal amount, OperationType operationType, TransactionStatus status,
                                     String transactionDate, String referenceNumber) {
}