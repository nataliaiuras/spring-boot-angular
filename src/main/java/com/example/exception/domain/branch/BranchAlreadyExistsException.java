package com.example.exception.domain.branch;

import com.example.exception.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class BranchAlreadyExistsException extends DomainEntityException {
    public BranchAlreadyExistsException(String name) {
        super("Branch with this name", name, "already exists", HttpStatus.CONFLICT);
    }
}
