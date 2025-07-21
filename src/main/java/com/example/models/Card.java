package com.example.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CARDS")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CARD_NUMBER")
    private String cardNumber;

    @Column(name = "CARD_HOLDER")
    private String cardHolder;

    @Column(name = "VALID_THRU")
    private Date validThru;

    @Column(name = "CVV_CODE")
    private int cvvCode;

    private int pin;

}
