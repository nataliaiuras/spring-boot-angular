package com.example.dto.response.account;

import java.math.BigDecimal;

public record AccountBalanceResponse(Long id, String accountNumber, BigDecimal balance, String currency,
                                     String lastModifiedDate) {
}
