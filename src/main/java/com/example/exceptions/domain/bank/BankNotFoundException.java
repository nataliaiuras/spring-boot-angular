package com.example.exceptions.domain.bank;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class BankNotFoundException extends DomainEntityException {

    public BankNotFoundException(Long id) {
        super("Bank", id.toString(), "not found", HttpStatus.NOT_FOUND);
    }
}
