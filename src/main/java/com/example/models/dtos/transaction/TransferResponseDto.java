package com.example.models.dtos.transaction;

import com.example.utils.enums.TransactionStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransferResponseDto {
    private Long transactionId;
    private String referenceNumber;
    private TransactionStatus status; // COMPLETED, PENDING, PROCESSING
    private BigDecimal amount;
    private String currency;
    private LocalDateTime processedAt;
    private LocalDateTime expectedCompletionDate;
    private BigDecimal fee;
    private String message;
}

