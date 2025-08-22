package com.example.exceptions.domain.user;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends DomainEntityException {

    public UserNotFoundException(Long userId) {
        super("User", userId.toString(), "not found", HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String username) {
        super("User", username, "not found", HttpStatus.NOT_FOUND);
    }
}
