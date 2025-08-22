package com.example.exceptions.domain.bank;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class BankAlreadyExistsException extends DomainEntityException {

    public BankAlreadyExistsException(Long id) {
        super("Bank", id.toString(), " already exists", HttpStatus.BAD_REQUEST);
    }
}
