package com.example.dto.response.card;




import com.example.dto.response.account.AccountResponse;
import com.example.util.enums.CardStatus;
import com.example.util.enums.CurrencyType;
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
public class CardDetailResponse {

    private Long id;

    @MaskSensitive(type = MaskSensitive.SensitiveDataType.CARD_NUMBER, fullMask = false)
    @JsonSerialize(using = SensitiveDataSerializer.class)
    private String cardNumber;

    private String cardHolder;

    private CurrencyType currency;

    private LocalDate expiryDate;

    @MaskSensitive(type = MaskSensitive.SensitiveDataType.CVV)
    @JsonSerialize(using = SensitiveDataSerializer.class)
    private int cvvCode;

    private CardStatus status;

    private String blockedReason;

    private AccountResponse account;

    private String createdDate;

    private String lastModifiedDate;

    private Long version;


}