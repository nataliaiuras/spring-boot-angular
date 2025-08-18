package com.example.dtos;


import com.example.models.Account;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class CardDto {

    private Long id;
    private String cardNumber;
    private String cardHolder;
    private LocalDate validThru;
    @JsonSerialize(as = Integer.class)
    private int cvvCode;
    @JsonSerialize(as = Integer.class)
    private int pin;
    private Account account;
    private Instant createdDate;
    private Instant lastModifiedDate;
    private Long version;

}
