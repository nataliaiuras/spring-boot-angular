package com.example.exceptions.domain.user;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends DomainEntityException {

    public InvalidCredentialsException() {
        super("User", "credentials", "are invalid", HttpStatus.UNAUTHORIZED);
    }
}