package com.example.exceptions.domain;

import com.example.exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public abstract class DomainEntityException extends BusinessException {

    protected DomainEntityException(String entityType, String identifier, String operation) {
        super(String.format("%s with identifier '%s' %s", entityType, identifier, operation),
                generateErrorCode(entityType, operation),
                HttpStatus.NOT_FOUND);
    }

    protected DomainEntityException(String entityType, String identifier, String operation, HttpStatus status) {
        super(String.format("%s with identifier '%s' %s", entityType, identifier, operation),
                generateErrorCode(entityType, operation),
                status);
    }

    protected DomainEntityException(String entityType, String identifier, String operation, String detail, HttpStatus status) {
        super(String.format("%s with identifier '%s' %s '%s'", entityType, identifier, operation, detail),
                generateErrorCode(entityType, operation),
                status);
    }

    private static String generateErrorCode(String entityType, String operation) {
        return String.format("%s_%s", entityType.toUpperCase(), operation.toUpperCase());
    }
}