package com.example.dto.request.transaction;

import com.example.util.enums.CurrencyType;
import com.example.util.enums.OperationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountOperationRequest {
    @NotNull(message = "AccountResponse ID is required")
    private Long accountId;

    @NotNull(message = "Operation type is required")
    private OperationType operationType;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "10.00", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Currency is required")
    private CurrencyType currency;

}

