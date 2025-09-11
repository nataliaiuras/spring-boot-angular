package com.example.dtos.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AccountBalanceDto {
    private Long id;
    private String accountNumber;
    private BigDecimal currentBalance;
    private BigDecimal availableBalance;
    private String currency;
    private String lastModifiedDate;

}
