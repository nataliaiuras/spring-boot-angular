package com.example.exceptions.domain.account;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class AccountInactiveException extends DomainEntityException {
  public AccountInactiveException(Long accountId) {
    super("Account: " , accountId.toString() ," is inactive ", HttpStatus.BAD_REQUEST);
  }
}

