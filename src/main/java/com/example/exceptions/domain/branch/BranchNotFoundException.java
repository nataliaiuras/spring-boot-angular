package com.example.exceptions.domain.branch;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class BranchNotFoundException extends DomainEntityException {
    public BranchNotFoundException(Long branchId) {
        super("Branch", branchId.toString(), "not found", HttpStatus.NOT_FOUND);
    }
}

