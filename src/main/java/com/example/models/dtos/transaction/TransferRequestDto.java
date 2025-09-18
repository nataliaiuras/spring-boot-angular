package com.example.models.dtos.transaction;

import com.example.utils.enums.CurrencyType;
import com.example.utils.enums.DestinationIdentifierType;
import com.example.utils.enums.TransferType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDto {
    @NotNull(message = "Source account ID is required")
    private Long fromAccountId;

    @NotNull(message = "Destination type is required")
    private DestinationIdentifierType destinationIdentifierType;

    @NotNull(message = "Destination is required")
    private Long destinationIdentifier; // account number, IBAN, card number, etc.

    @NotNull(message = "Transfer type is required")
    private TransferType transferType;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Currency is required")
    private CurrencyType currency;

    private String description;

    private String beneficiaryName;
    private String beneficiaryAddress;
    private String beneficiaryBank;
    private String bankCode;
    private String bicCode;

}

