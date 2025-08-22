package com.example.exceptions.domain.branch;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class BranchAlreadyExistsException extends DomainEntityException {
    public BranchAlreadyExistsException(Long id) {
        super("Branch with id ", id.toString(), " already exists", HttpStatus.CONFLICT);
    }
}
