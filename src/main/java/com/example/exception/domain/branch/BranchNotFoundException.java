package com.example.exception.domain.branch;

import com.example.exception.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class BranchNotFoundException extends DomainEntityException {
    public BranchNotFoundException(Long branchId) {
        super("Branch", branchId.toString(), "not found", HttpStatus.NOT_FOUND);
    }
}

