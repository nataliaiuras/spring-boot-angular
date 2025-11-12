package com.example.exception.domain.transaction;

import com.example.exception.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public class InsufficientBalanceException extends DomainEntityException {

    public InsufficientBalanceException(Long accountId, BigDecimal amount) {
        super("Insufficient balance in account: ", accountId.toString(),
                " Required: ", amount.toString(), HttpStatus.BAD_REQUEST);
    }

    public InsufficientBalanceException(String cardNumber) {
        super("Card", maskCardNumber(cardNumber), "has insufficient funds", HttpStatus.BAD_REQUEST);
    }

    private static String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "****" + cardNumber.substring(cardNumber.length() - 4);
    }
}

