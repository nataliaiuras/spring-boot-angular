package com.example.exception.domain.transaction;

import com.example.exception.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class TransactionNotFoundException extends DomainEntityException {
    public TransactionNotFoundException(Long transactionId) {
        super("Transaction", transactionId.toString(), "not found", HttpStatus.NOT_FOUND);
    }
}
