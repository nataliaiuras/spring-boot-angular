package com.example.dtos.account;

import com.example.utils.AccountType;
import com.example.utils.CurrencyType;

import java.math.BigDecimal;

public record AccountOverviewDto(Long id, String accountNumber, AccountType type, CurrencyType currency,
                                 String ibanCode, BigDecimal currentBalance, BigDecimal availableBalance,
                                 BigDecimal creditLimit, boolean active, BigDecimal dailyTransferLimit) {
}