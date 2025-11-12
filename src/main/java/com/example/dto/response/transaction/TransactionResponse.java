package com.example.dto.response.transaction;

import com.example.util.enums.OperationType;
import com.example.util.enums.TransactionStatus;

import java.math.BigDecimal;

public record TransactionResponse(Long id, BigDecimal amount, OperationType operationType, TransactionStatus status,
                                  String transactionDate, String referenceNumber) {
}