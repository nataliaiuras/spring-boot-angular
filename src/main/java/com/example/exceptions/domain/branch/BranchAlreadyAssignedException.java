package com.example.exceptions.domain.branch;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class BranchAlreadyAssignedException extends DomainEntityException {
    public BranchAlreadyAssignedException(Long branchId) {
        super("Branch", branchId.toString(), " is already assigned to bank", HttpStatus.CONFLICT);
    }
}
