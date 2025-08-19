package com.example.dtos.overview;

import com.example.utils.AccountType;
import com.example.utils.Currency;

import java.math.BigDecimal;

public record AccountOverviewDto(long id, String accountNumber, AccountType type, Currency currency, String ibanCode,
                                 BigDecimal balance) {
}
