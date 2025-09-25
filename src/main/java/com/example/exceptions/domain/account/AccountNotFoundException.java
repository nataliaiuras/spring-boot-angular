package com.example.exceptions.domain.account;

import com.example.exceptions.domain.DomainEntityException;

public class AccountNotFoundException extends DomainEntityException {

    public AccountNotFoundException(Long accountId) {
        super("Account", accountId.toString(), "not found");
    }

    public AccountNotFoundException(String accountIdentifier) {
        super("Account", accountIdentifier, "not found");
    }

}