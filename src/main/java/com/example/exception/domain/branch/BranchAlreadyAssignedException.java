package com.example.exception.domain.branch;

import com.example.exception.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class BranchAlreadyAssignedException extends DomainEntityException {
    public BranchAlreadyAssignedException(Long branchId) {
        super("Branch", branchId.toString(), " is already assigned to bank", HttpStatus.CONFLICT);
    }
}
