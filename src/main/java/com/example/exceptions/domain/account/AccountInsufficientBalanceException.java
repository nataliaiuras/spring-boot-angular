package com.example.exceptions.domain.account;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class AccountInsufficientBalanceException extends DomainEntityException {

    public AccountInsufficientBalanceException(String accountNumber) {
        super("Account", accountNumber, "has insufficient balance", HttpStatus.BAD_REQUEST);
    }
}