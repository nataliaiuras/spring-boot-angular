package com.example.dto.response.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Getter
public class TransactionCategoryResponse {
    private String category;
    private BigDecimal totalAmount;
    private Integer transactionCount;
    private BigDecimal averageAmount;
    private Double percentage;
}

