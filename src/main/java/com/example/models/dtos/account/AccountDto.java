package com.example.models.dtos.account;

import com.example.models.dtos.card.CardOverviewDto;
import com.example.models.dtos.common.MetadataDto;
import com.example.models.dtos.customer.CustomerOverviewDto;
import com.example.utils.enums.AccountType;
import com.example.utils.enums.CurrencyType;
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
    private String ibanCode;
    private AccountType type;
    private CurrencyType currency;
    private BigDecimal balance;
    private boolean active;
    private CustomerOverviewDto customer;
    private CardOverviewDto card;
    private MetadataDto metadata;

}