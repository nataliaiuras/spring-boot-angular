package com.example.controller;


import com.example.dto.request.transaction.TransactionCreateRecurringRequest;
import com.example.dto.response.transaction.*;
import com.example.service.TransactionService;
import com.example.dto.response.general.ApiResponse;
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

import java.time.LocalDate;
import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/transactions")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getAllTransactions(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<TransactionResponse> responsePage = transactionService.getAllTransactions(pageable);
        return ResponseEntity.ok(ApiResponse.success(responsePage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransaction(@PathVariable Long id) {
        TransactionResponse response = transactionService.getTransactionById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getTransactionsByAccount(
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
        Page<TransactionResponse> responsePage = transactionService.getTransactionsByAccount(
                accountId, startDate, endDate, type, status, pageable);
        return ResponseEntity.ok(ApiResponse.success(responsePage));
    }

    @GetMapping("/account/{accountId}/period")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getTransactionsPeriodByAccount(
            @PathVariable Long accountId,
            @RequestParam @NotNull(message = "Start date is required") LocalDate startDate,
            @RequestParam @NotNull(message = "End date is required") LocalDate endDate,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<TransactionResponse> responsePage = transactionService.getTransactionsPeriodByAccount(accountId, startDate, endDate, pageable);
        return ResponseEntity.ok(ApiResponse.success(responsePage));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<TransactionDetailResponse>> cancelTransaction(@PathVariable Long id) {
        TransactionDetailResponse response = transactionService.cancelTransaction(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Transaction cancelled successfully"));
    }

    @PutMapping("/{id}/reverse")
    public ResponseEntity<ApiResponse<TransactionDetailResponse>> reverseTransaction(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        TransactionDetailResponse response = transactionService.reverseTransaction(id, reason);
        return ResponseEntity.ok(ApiResponse.success(response, "Transaction reversed successfully"));
    }

    @PostMapping("/recurring")
    public ResponseEntity<ApiResponse<RecurringTransactionResponse>> createRecurringTransaction(
            @Valid @RequestBody TransactionCreateRecurringRequest request) {

        RecurringTransactionResponse response = transactionService.createRecurringTransaction(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Recurring transaction created successfully"));
    }

    @GetMapping("/recurring/account/{accountId}")
    public ResponseEntity<ApiResponse<Page<RecurringTransactionResponse>>> getRecurringTransactions(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("nextExecutionDate").ascending());
        Page<RecurringTransactionResponse> responsePage = transactionService.getRecurringTransactions(accountId, pageable);
        return ResponseEntity.ok(ApiResponse.success(responsePage));
    }

    @GetMapping("/analytics/account/{accountId}/summary")
    public ResponseEntity<ApiResponse<TransactionSummaryResponse>> getTransactionSummary(
            @PathVariable Long accountId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        TransactionSummaryResponse response = transactionService.getTransactionSummary(accountId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/analytics/account/{accountId}/categories")
    public ResponseEntity<ApiResponse<List<TransactionCategoryResponse>>> getTransactionsByCategory(
            @PathVariable Long accountId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        List<TransactionCategoryResponse> categoryBreakdown = transactionService.getTransactionsByCategory(accountId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(categoryBreakdown));
    }
}
