package com.example.dto.response.transaction;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
public class RecurringTransactionResponse {
    private Long id;
    private Long sourceAccountId;
    private Long destinationAccountId;
    private BigDecimal amount;
    private String description;
    private String frequency; // DAILY, WEEKLY, MONTHLY, YEARLY
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextExecutionDate;
    private String status; // ACTIVE, PAUSED, CANCELLED
    private Integer executionCount;
    private Integer maxExecutions;
}

