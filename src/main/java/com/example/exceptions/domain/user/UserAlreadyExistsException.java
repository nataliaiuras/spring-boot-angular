package com.example.exceptions.domain.user;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends DomainEntityException {

    public UserAlreadyExistsException(String username) {
        super("User", username, "already exists", HttpStatus.CONFLICT);
    }
}