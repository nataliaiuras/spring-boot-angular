package com.example.models.dtos.transaction;

import com.example.utils.enums.OperationType;
import com.example.utils.enums.CurrencyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountOperationRequestDto {
    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotNull(message = "Operation type is required")
    private OperationType operationType;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "10.00", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Currency is required")
    private CurrencyType currency;

}

