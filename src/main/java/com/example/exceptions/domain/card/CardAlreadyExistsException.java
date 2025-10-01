package com.example.exceptions.domain.card;

import com.example.exceptions.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

public class CardAlreadyExistsException extends DomainEntityException {

    public CardAlreadyExistsException(Long id) {
        super("Card for this account id: ", id.toString(), "already issued", HttpStatus.CONFLICT);
    }
}
