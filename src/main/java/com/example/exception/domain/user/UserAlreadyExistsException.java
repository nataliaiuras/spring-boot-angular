package com.example.exception.domain.user;

import com.example.exception.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends DomainEntityException {

    public UserAlreadyExistsException(String username) {
        super("User", username, "already exists", HttpStatus.CONFLICT);
    }
}