package com.example.exceptions.domain.bank;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class BankAlreadyExistsException extends DomainEntityException {

    public BankAlreadyExistsException(String name) {
        super("Bank", name, " already exists", HttpStatus.BAD_REQUEST);
    }
}
