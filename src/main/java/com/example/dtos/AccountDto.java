package com.example.dtos;

import com.example.dtos.overview.CardOverviewDto;
import com.example.dtos.overview.CustomerOverviewDto;
import com.example.utils.AccountType;
import com.example.utils.Currency;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class AccountDto {

    private Long id;
    private String accountNumber;
    private AccountType type;
    private Currency currency;
    private String ibanCode;
    private BigDecimal balance;
    private CustomerOverviewDto customer;
    private CardOverviewDto card;

}
