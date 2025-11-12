package com.example.exception.domain.customer;

import com.example.exception.domain.DomainEntityException;

public class CustomerNotFoundException extends DomainEntityException {

    public CustomerNotFoundException(Long customerId) {
        super("Customer", customerId.toString(), "not found");
    }

    public CustomerNotFoundException(String customerEmail) {
        super("Customer", customerEmail, "not found");
    }
}

