package com.example.dtos;

import com.example.models.Account;
import lombok.*;

import java.util.Date;

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

    private Date validThru;

    private int cvvCode;

    private int pin;

}
