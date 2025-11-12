package com.example.controller;

import com.example.dto.request.transaction.TransferRequest;
import com.example.dto.response.transaction.TransactionResponse;
import com.example.dto.response.transaction.TransferResponse;
import com.example.service.OperationService;
import com.example.util.enums.CurrencyType;
import com.example.dto.response.general.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/operation")
@Validated
@RequiredArgsConstructor
@Slf4j
public class OperationController {

    private final OperationService operationService;

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionResponse>> deposit(
            @RequestParam Long accountId,
            @RequestParam BigDecimal amount,
            @RequestParam CurrencyType currencyType,
            @RequestParam(required = false) String description) {

        TransactionResponse response = operationService.deposit(accountId, amount, description, currencyType);
        return ResponseEntity.ok(ApiResponse.success(response, "Deposit completed successfully"));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<TransactionResponse>> withdraw(
            @RequestParam Long accountId,
            @RequestParam BigDecimal amount,
            @RequestParam CurrencyType currencyType,
            @RequestParam(required = false) String description) {

        TransactionResponse response = operationService.withdraw(accountId, amount, description, currencyType);
        return ResponseEntity.ok(ApiResponse.success(response, "Withdrawal completed successfully"));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransferResponse>> transfer(@Valid @RequestBody TransferRequest request) {
        TransferResponse response = operationService.processTransfer(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Transfer initiated successfully"));
    }
}
