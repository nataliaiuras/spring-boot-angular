package com.example.exception.domain.user;

import com.example.exception.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends DomainEntityException {

    public InvalidCredentialsException() {
        super("User", "credentials", "are invalid", HttpStatus.UNAUTHORIZED);
    }
}