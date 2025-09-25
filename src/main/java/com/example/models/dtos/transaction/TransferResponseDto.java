package com.example.models.dtos.transaction;

import com.example.utils.enums.CurrencyType;
import com.example.utils.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferResponseDto {

    private String referenceNumber;
    private TransactionStatus status;
    private Instant transactionDate;
    private BigDecimal sourceAmount;
    private CurrencyType sourceCurrency;
    private CurrencyType destinationCurrency;
    private BigDecimal conversionRate;
    private BigDecimal conversionFee;
    private BigDecimal convertedAmount;
    private BigDecimal operationFee;
    private BigDecimal destinationAmount;

}

