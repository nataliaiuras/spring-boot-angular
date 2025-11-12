package com.example.dto.response.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
public class TransactionSummaryResponse {
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

