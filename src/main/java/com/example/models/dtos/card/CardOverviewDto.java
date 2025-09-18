package com.example.models.dtos.card;

import java.util.Date;

public record CardOverviewDto(Long id, String cardNumber, String cardHolder, Date validThru, int cvvCode, int pin) {
}