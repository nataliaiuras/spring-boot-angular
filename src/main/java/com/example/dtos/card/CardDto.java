package com.example.dtos.card;


import com.example.dtos.account.AccountOverviewDto;
import com.example.utils.MaskSensitive;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CardDto {

    private Long id;
    private String cardNumber;
    private String cardHolder;
    private LocalDate validThru;
    @JsonSerialize(as = Integer.class)
    @MaskSensitive(maskWith = "***")
    private int cvvCode;
    @JsonSerialize(as = Integer.class)
    @MaskSensitive(maskWith = "***")
    private int pin;
    private AccountOverviewDto account;

}