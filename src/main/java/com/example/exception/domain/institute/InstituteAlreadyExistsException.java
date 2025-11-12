package com.example.exception.domain.institute;

import com.example.exception.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class InstituteAlreadyExistsException extends DomainEntityException {

    public InstituteAlreadyExistsException(String name) {
        super("Institute", name, " already exists", HttpStatus.BAD_REQUEST);
    }
}
