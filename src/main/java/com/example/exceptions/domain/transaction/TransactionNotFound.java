package com.example.exceptions.domain.transaction;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class TransactionNotFound extends DomainEntityException {

    public TransactionNotFound(Long transactionId) {
        super("Transaction", transactionId.toString(), "not found", HttpStatus.NOT_FOUND);
    }
}
