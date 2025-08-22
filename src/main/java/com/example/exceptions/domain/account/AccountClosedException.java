package com.example.exceptions.domain.account;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class AccountClosedException extends DomainEntityException {

    public AccountClosedException(String accountNumber) {
        super("Account", accountNumber, "is closed", HttpStatus.BAD_REQUEST);
    }
}