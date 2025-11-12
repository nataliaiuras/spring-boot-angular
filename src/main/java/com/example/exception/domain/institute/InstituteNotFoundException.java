package com.example.exception.domain.institute;

import com.example.exception.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class InstituteNotFoundException extends DomainEntityException {

    public InstituteNotFoundException(Long id) {
        super("Institute", id.toString(), "not found", HttpStatus.NOT_FOUND);
    }
}
