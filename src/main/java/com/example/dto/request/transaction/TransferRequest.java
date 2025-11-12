package com.example.dto.request.transaction;


import com.example.util.enums.OperationType;
import com.example.util.enums.RecipientIdentifierType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Getter
public class TransferRequest {
    @NotNull(message = "Source account ID is required")
    private Long sourceAccountId;

    @NotNull(message = "Operation type is required")
    private OperationType operationType;

    @NotNull(message = "Recipient identifier type is required")
    private RecipientIdentifierType recipientIdentifierType;

    @NotNull(message = "Recipient identifier is required")
    private String recipientIdentifier;

    @NotNull(message = "Bic code is required")
    private String bicCode;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

   /* @NotNull(message = "Amount currency specification is required")
    private CurrencyType amountCurrency;

    @NotNull(message = "Source currency is required")
    private CurrencyType sourceCurrency;

    @NotNull(message = "Destination currency is required")
    private CurrencyType destinationCurrency;*/

    @NotNull(message = "Description is required")
    private String description;

    private String recipientFirstName;
    private String recipientLastName;


}

