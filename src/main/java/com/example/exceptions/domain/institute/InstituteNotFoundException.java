package com.example.exceptions.domain.institute;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class InstituteNotFoundException extends DomainEntityException {

    public InstituteNotFoundException(Long id) {
        super("Institute", id.toString(), "not found", HttpStatus.NOT_FOUND);
    }
}
