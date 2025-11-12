package com.example.dto.response.card;


import com.example.util.enums.CardStatus;
import com.example.util.mask.MaskSensitive;
import com.example.util.mask.SensitiveDataSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
public class CardResponse {

    private Long id;

    @MaskSensitive(type = MaskSensitive.SensitiveDataType.CARD_NUMBER, fullMask = false)
    @JsonSerialize(using = SensitiveDataSerializer.class)
    private String cardNumber;

    private String cardHolder;

    private LocalDate expiryDate;

    private CardStatus status;

    private String createdDate;

}