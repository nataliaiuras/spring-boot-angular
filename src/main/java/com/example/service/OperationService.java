package com.example.service;

import com.example.dto.request.transaction.TransferRequest;
import com.example.dto.response.transaction.TransactionResponse;
import com.example.dto.response.transaction.TransferResponse;
import com.example.util.enums.CurrencyType;
import jakarta.validation.Valid;

import java.math.BigDecimal;

public interface OperationService {

    TransferResponse processTransfer(@Valid TransferRequest request);

    TransactionResponse deposit(Long accountId, BigDecimal amount, String description, CurrencyType currencyType);

    TransactionResponse withdraw(Long accountId, BigDecimal amount, String description, CurrencyType currencyType);

}
