package com.example.exceptions;

import lombok.Getter;

@Getter
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }

    private double amount;

    public InsufficientFundsException(double amount) {
        this.amount = amount;
    }

}
