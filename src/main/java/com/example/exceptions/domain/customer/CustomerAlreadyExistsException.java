package com.example.exceptions.domain.customer;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class CustomerAlreadyExistsException extends DomainEntityException {
    public CustomerAlreadyExistsException(String email) {
        super("Customer", email, "already exists", HttpStatus.CONFLICT);
    }
}