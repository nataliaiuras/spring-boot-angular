package com.example.exception.domain.transaction;

import com.example.exception.domain.DomainEntityException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public class DailyTransferLimitExceededException extends DomainEntityException {
    public DailyTransferLimitExceededException(Long accountId, BigDecimal limit) {
        super("Daily transfer limit exceeded for account: ", accountId.toString(),
                ". Limit: ", limit.toString(), HttpStatus.BAD_REQUEST);
    }
}

