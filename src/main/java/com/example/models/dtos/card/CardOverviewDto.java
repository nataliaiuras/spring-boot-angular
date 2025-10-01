package com.example.models.dtos.card;

import com.example.utils.enums.CardStatus;
import com.example.utils.mask.MaskSensitive;
import com.example.utils.mask.SensitiveDataSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardOverviewDto {
    private Long id;

    @MaskSensitive(type = MaskSensitive.SensitiveDataType.CARD_NUMBER, fullMask = false)
    @JsonSerialize(using = SensitiveDataSerializer.class)
    private String cardNumber;

    private String cardHolder;
    private LocalDate expiryDate;
    private CardStatus status;

    private Instant createdDate;
}