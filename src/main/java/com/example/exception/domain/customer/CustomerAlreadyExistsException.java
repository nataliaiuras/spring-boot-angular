package com.example.exception.domain.customer;

import com.example.exception.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class CustomerAlreadyExistsException extends DomainEntityException {
    public CustomerAlreadyExistsException(String email) {
        super("Customer", email, "already exists", HttpStatus.CONFLICT);
    }
}