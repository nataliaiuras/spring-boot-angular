package com.example.exception.domain.card;

import com.example.exception.domain.DomainEntityException;

public class CardNotFoundException extends DomainEntityException {

    public CardNotFoundException(Long cardId) {
        super("Card", cardId.toString(), "not found");
    }
    
    public CardNotFoundException(String cardNumber) {
        super("Card", maskCardNumber(cardNumber), "not found");
    }
    
    private static String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "****" + cardNumber.substring(cardNumber.length() - 4);
    }
}