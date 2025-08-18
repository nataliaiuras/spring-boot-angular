package com.example.dtos;

import com.example.models.Card;
import com.example.models.Customer;
import com.example.utils.AccountType;
import com.example.utils.Currency;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

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
    private Customer customer;
    private Card card;
    private Instant createdDate;
    private Instant lastModifiedDate;
    private Long version;

}
