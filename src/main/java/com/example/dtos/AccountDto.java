package com.example.dtos;

import com.example.models.Card;
import com.example.models.Client;
import com.example.utils.AccountType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class AccountDto {

    private long id;
    private String accountNumber;
    private AccountType type;
    private String ibanCode;
    private double balance;
    private Card card;

}
