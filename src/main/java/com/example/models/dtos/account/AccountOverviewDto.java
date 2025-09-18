package com.example.models.dtos.account;

import com.example.utils.enums.AccountType;
import com.example.utils.enums.CurrencyType;

import java.math.BigDecimal;

public record AccountOverviewDto(Long id, String accountNumber, AccountType type, CurrencyType currency,
                                 String ibanCode, BigDecimal balance, boolean active) {
}