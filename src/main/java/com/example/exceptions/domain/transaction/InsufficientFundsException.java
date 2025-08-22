package com.example.exceptions.domain.transaction;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class InsufficientFundsException extends DomainEntityException {

    public InsufficientFundsException(String cardNumber) {
        super("Card", maskCardNumber(cardNumber), "has insufficient funds", HttpStatus.BAD_REQUEST);
    }
    
    private static String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "****" + cardNumber.substring(cardNumber.length() - 4);
    }
}