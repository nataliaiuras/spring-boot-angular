package com.example.dtos.account;

import com.example.dtos.card.CardOverviewDto;
import com.example.dtos.customer.CustomerOverviewDto;
import com.example.utils.AccountType;
import com.example.utils.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AccountDto {

    private Long id;
    private String accountNumber;
    private AccountType type;
    private CurrencyType currency;
    private String ibanCode;
    private BigDecimal currentBalance;
    private BigDecimal availableBalance;
    private BigDecimal creditLimit;
    private boolean active;
    private BigDecimal dailyTransferLimit;
    private CustomerOverviewDto customer;
    private CardOverviewDto card;

}