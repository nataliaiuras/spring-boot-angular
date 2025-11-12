package com.example.dto.response.account;

import com.example.dto.response.card.CardResponse;
import com.example.dto.response.customer.CustomerResponse;
import com.example.util.enums.AccountType;
import com.example.util.enums.CurrencyType;

import java.math.BigDecimal;
import java.util.Set;

public record AccountDetailResponse(Long id, String accountNumber, String ibanCode, AccountType type,
                                    CurrencyType currency, BigDecimal balance, boolean active,
                                    CustomerResponse customer, Set<CardResponse> cards, String lastModifiedDate,
                                    String createdDate, Long version) {
}