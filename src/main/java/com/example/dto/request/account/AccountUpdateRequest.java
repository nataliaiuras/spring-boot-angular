package com.example.dto.request.account;

import com.example.util.enums.AccountType;
import com.example.util.enums.CurrencyType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountUpdateRequest {

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @NotNull(message = "Customer id is required")
    @Valid
    private Long customerId;
}
