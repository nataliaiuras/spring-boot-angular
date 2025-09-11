package com.example.dtos.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummaryDto {
    private Long accountId;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private BigDecimal totalCredits;
    private BigDecimal totalDebits;
    private BigDecimal netAmount;
    private Integer totalTransactions;
    private Integer creditCount;
    private Integer debitCount;
    private BigDecimal averageTransactionAmount;
    private BigDecimal largestCredit;
    private BigDecimal largestDebit;
}

