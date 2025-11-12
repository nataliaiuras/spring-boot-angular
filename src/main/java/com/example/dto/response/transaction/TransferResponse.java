package com.example.dto.response.transaction;


import com.example.util.enums.CurrencyType;
import com.example.util.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@Builder
@Getter
public class TransferResponse {

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

