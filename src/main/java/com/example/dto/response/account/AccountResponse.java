package com.example.dto.response.account;

import java.math.BigDecimal;

public record AccountResponse(Long id, String accountNumber, String ibanCode, com.example.util.enums.AccountType type, com.example.util.enums.CurrencyType currency,
                              BigDecimal balance, boolean active, String lastModifiedDate) {
}