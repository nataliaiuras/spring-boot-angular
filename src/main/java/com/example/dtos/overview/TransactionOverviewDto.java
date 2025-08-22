package com.example.dtos.overview;

import com.example.utils.Status;
import com.example.utils.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionOverviewDto(Long id, BigDecimal amount, TransactionType type, Status status,
                                     Instant transactionDate, String referenceNumber) {
}