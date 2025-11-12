package com.example.exception.domain.address;

import com.example.exception.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class AddressIsAlreadyAssignedException extends DomainEntityException {
    public AddressIsAlreadyAssignedException(Long id) {

        super("Address", id.toString(), "is already assigned", HttpStatus.CONFLICT);
    }
}
