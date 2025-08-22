package com.example.exceptions.domain.customer;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class CustomerAccountLockedException extends DomainEntityException {
    public CustomerAccountLockedException(Long customerId) {
        super("Customer", customerId.toString(), "account is locked", HttpStatus.FORBIDDEN);
    }
}