package com.example.exceptions.domain.address;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class AddressIsAlreadyAssignedException extends DomainEntityException {
    public AddressIsAlreadyAssignedException(Long id) {

        super("Address", id.toString(), "is already assigned", HttpStatus.CONFLICT);
    }
}
