package com.example.models.dtos.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCategoryDto {
    private String category;
    private BigDecimal totalAmount;
    private Integer transactionCount;
    private BigDecimal averageAmount;
    private Double percentage; // Percentage of total spending
}

