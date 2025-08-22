package com.example.exceptions.domain.address;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class AddressNotFoundException extends DomainEntityException {

    public AddressNotFoundException(Long id) {
        super("Address", id.toString(), "not found", HttpStatus.NOT_FOUND);
    }
}