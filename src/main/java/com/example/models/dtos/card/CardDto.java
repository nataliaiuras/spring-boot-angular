package com.example.models.dtos.card;


import com.example.models.dtos.account.AccountOverviewDto;
import com.example.utils.enums.CardStatus;
import com.example.utils.mask.MaskSensitive;
import com.example.utils.mask.SensitiveDataSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardDto {

    private Long id;

    @MaskSensitive(type = MaskSensitive.SensitiveDataType.CARD_NUMBER, fullMask = false)
    @JsonSerialize(using = SensitiveDataSerializer.class)
    private String cardNumber;

    private String cardHolder;

    private LocalDate expiryDate;

    @MaskSensitive(type = MaskSensitive.SensitiveDataType.CVV)
    @JsonSerialize(using = SensitiveDataSerializer.class)
    private int cvvCode;

    private CardStatus status;

    private String blockedReason;

    private AccountOverviewDto account;

}