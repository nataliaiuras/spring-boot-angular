package com.example.controllers;


import com.example.exceptions.response.ApiResponse;
import com.example.models.dtos.transaction.*;
import com.example.services.TransactionService;
import com.example.utils.enums.CurrencyType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/transactions")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TransactionOverviewDto>>> getAllTransactions(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<TransactionOverviewDto> transactions = transactionService.getAllTransactions(pageable);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionOverviewDto>> getTransaction(@PathVariable Long id) {
        TransactionOverviewDto transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(ApiResponse.success(transaction));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<ApiResponse<Page<TransactionOverviewDto>>> getTransactionsByAccount(
            @PathVariable Long accountId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String type, // CREDIT, DEBIT, TRANSFER
            @RequestParam(required = false) String status, // PENDING, COMPLETED, FAILED
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<TransactionOverviewDto> transactions = transactionService.getTransactionsByAccount(
                accountId, startDate, endDate, type, status, pageable);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @GetMapping("/account/{accountId}/period")
    public ResponseEntity<ApiResponse<Page<TransactionOverviewDto>>> getTransactionsPeriodByAccount(
            @PathVariable Long accountId,
            @RequestParam @NotNull(message = "Start date is required") LocalDate startDate,
            @RequestParam @NotNull(message = "End date is required") LocalDate endDate,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<TransactionOverviewDto> transactions = transactionService.getTransactionsPeriodByAccount(accountId, startDate, endDate, pageable);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransferResponseDto>> transfer(@Valid @RequestBody TransferRequestDto transferRequest) {
        TransferResponseDto response = transactionService.processTransfer(transferRequest);
        return ResponseEntity.ok(ApiResponse.success(response, "Transfer initiated successfully"));
    }

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionDto>> deposit(
            @RequestParam Long accountId,
            @RequestParam BigDecimal amount,
            @RequestParam CurrencyType currencyType,
            @RequestParam(required = false) String description) {

        TransactionDto transaction = transactionService.deposit(accountId, amount, description, currencyType);
        return ResponseEntity.ok(ApiResponse.success(transaction, "Deposit completed successfully"));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<TransactionDto>> withdraw(
            @RequestParam Long accountId,
            @RequestParam BigDecimal amount,
            @RequestParam CurrencyType currencyType,
            @RequestParam(required = false) String description) {

        TransactionDto transaction = transactionService.withdraw(accountId, amount, description, currencyType);
        return ResponseEntity.ok(ApiResponse.success(transaction, "Withdrawal completed successfully"));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<TransactionDto>> cancelTransaction(@PathVariable Long id) {
        TransactionDto transaction = transactionService.cancelTransaction(id);
        return ResponseEntity.ok(ApiResponse.success(transaction, "Transaction cancelled successfully"));
    }

    @PutMapping("/{id}/reverse")
    public ResponseEntity<ApiResponse<TransactionDto>> reverseTransaction(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {

        TransactionDto transaction = transactionService.reverseTransaction(id, reason);
        return ResponseEntity.ok(ApiResponse.success(transaction, "Transaction reversed successfully"));
    }

    @PostMapping("/recurring")
    public ResponseEntity<ApiResponse<TransactionDto>> createRecurringTransaction(
            @Valid @RequestBody RecurringTransactionDto recurringTransactionDto) {

        TransactionDto transaction = transactionService.createRecurringTransaction(recurringTransactionDto);
        return ResponseEntity.ok(ApiResponse.success(transaction, "Recurring transaction created successfully"));
    }

    @GetMapping("/recurring/account/{accountId}")
    public ResponseEntity<ApiResponse<Page<RecurringTransactionDto>>> getRecurringTransactions(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("nextExecutionDate").ascending());
        Page<RecurringTransactionDto> recurringTransactions = transactionService.getRecurringTransactions(accountId, pageable);
        return ResponseEntity.ok(ApiResponse.success(recurringTransactions));
    }

    @GetMapping("/analytics/account/{accountId}/summary")
    public ResponseEntity<ApiResponse<TransactionSummaryDto>> getTransactionSummary(
            @PathVariable Long accountId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        TransactionSummaryDto summary = transactionService.getTransactionSummary(accountId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    @GetMapping("/analytics/account/{accountId}/categories")
    public ResponseEntity<ApiResponse<List<TransactionCategoryDto>>> getTransactionsByCategory(
            @PathVariable Long accountId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        List<TransactionCategoryDto> categoryBreakdown = transactionService.getTransactionsByCategory(accountId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(categoryBreakdown));
    }
}
