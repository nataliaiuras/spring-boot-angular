package com.example.dtos.account;

import com.example.utils.AccountType;
import com.example.utils.CurrencyType;

public class AccountRequestDto {
    private Long id;
    private String accountNumber;
    private AccountType type;
    private CurrencyType currency;
    private String ibanCode;
    private Long customerId;
}
