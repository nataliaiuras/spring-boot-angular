package com.example.models.dtos.account;

import com.example.utils.enums.AccountType;
import com.example.utils.enums.CurrencyType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequestDto {

    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @NotNull(message = "Customer id is required")
    @Valid
    private Long customerId;
}
