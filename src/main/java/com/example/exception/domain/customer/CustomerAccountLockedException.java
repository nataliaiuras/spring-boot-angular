package com.example.exception.domain.customer;

import com.example.exception.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class CustomerAccountLockedException extends DomainEntityException {
    public CustomerAccountLockedException(Long customerId) {
        super("Customer", customerId.toString(), "account is locked", HttpStatus.FORBIDDEN);
    }
}