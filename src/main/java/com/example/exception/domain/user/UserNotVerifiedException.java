package com.example.exception.domain.user;

import com.example.exception.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class UserNotVerifiedException extends DomainEntityException {

    public UserNotVerifiedException(String username) {
        super("User", username, "is not verified", HttpStatus.FORBIDDEN);
    }
}