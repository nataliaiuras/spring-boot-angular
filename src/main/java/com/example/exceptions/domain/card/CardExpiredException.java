package com.example.exceptions.domain.card;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class CardExpiredException extends DomainEntityException {

    public CardExpiredException(String cardNumber) {
        super("Card", maskCardNumber(cardNumber), "has expired", HttpStatus.BAD_REQUEST);
    }

    private static String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "****" + cardNumber.substring(cardNumber.length() - 4);
    }
}