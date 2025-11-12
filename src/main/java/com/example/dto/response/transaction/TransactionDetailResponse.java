package com.example.dto.response.transaction;


import com.example.dto.response.account.AccountResponse;
import com.example.util.enums.CurrencyType;
import com.example.util.enums.OperationType;
import com.example.util.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@Builder
@Getter
public class TransactionDetailResponse {
    private Long id;
    private OperationType operationType;
    private AccountResponse sourceAccount;
    private AccountResponse destinationAccount;
    private BigDecimal sourceAmount;
    private CurrencyType sourceCurrency;
    private CurrencyType destinationCurrency;
    private BigDecimal conversionRate;
    private BigDecimal conversionFee;
    private BigDecimal convertedAmount;
    private BigDecimal operationFee;
    private BigDecimal destinationAmount;
    private String referenceNumber;
    private String description;
    private TransactionStatus status;
    private Date transactionDate;
    private String createdDate;
    private String lastModifiedDate;
    private Long version;

}