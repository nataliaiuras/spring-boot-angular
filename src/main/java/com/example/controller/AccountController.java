package com.example.controller;

import com.example.dto.request.account.AccountCreateRequest;
import com.example.dto.request.account.AccountUpdateRequest;
import com.example.dto.response.account.AccountBalanceResponse;
import com.example.dto.response.account.AccountDetailResponse;
import com.example.dto.response.account.AccountResponse;
import com.example.dto.response.card.CardResponse;
import com.example.dto.response.customer.CustomerResponse;
import com.example.dto.response.general.ApiResponse;
import com.example.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AccountResponse>>> getAllAccounts(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<AccountResponse> responsePage = accountService.getAllAccounts(pageable);
        return ResponseEntity.ok(ApiResponse.success(responsePage));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(@PathVariable Long id) {
        AccountResponse accountResponse = accountService.getAccountById(id);
        return ResponseEntity.ok(ApiResponse.success(accountResponse));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AccountDetailResponse>> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        AccountDetailResponse response = accountService.createAccount(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(ApiResponse.success(response, "AccountResponse created successfully"));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<AccountDetailResponse>> updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody AccountUpdateRequest request) {
        AccountDetailResponse response = accountService.updateAccount(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "AccountResponse updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok(ApiResponse.success("AccountResponse deleted successfully"));
    }

    @PutMapping("{id}/deactivate")
    public ResponseEntity<ApiResponse<String>> deactivateAccount(@PathVariable Long id) {
        accountService.deactivateAccount(id);
        return ResponseEntity.ok(ApiResponse.success("AccountResponse deactivated successfully"));
    }

    @GetMapping("{id}/card")
    public ResponseEntity<ApiResponse<Set<CardResponse>>> getCards(@PathVariable Long id) {
        Set<CardResponse> responseSet = accountService.getCardsByAccountId(id);
        return ResponseEntity.ok(ApiResponse.success(responseSet));
    }

    @GetMapping("{id}/customer")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomer(@PathVariable Long id) {
        CustomerResponse response = accountService.getCustomerByAccountId(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("{id}/balance")
    public ResponseEntity<ApiResponse<AccountBalanceResponse>> getAccountBalance(@PathVariable Long id) {
        AccountBalanceResponse response = accountService.getAccountBalance(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("{id}/balance/can-withdraw")
    public ResponseEntity<ApiResponse<Boolean>> canWithdraw(
            @PathVariable Long id,
            @RequestParam @DecimalMin(value = "0.01", message = "Amount must be greater than 0") BigDecimal amount) {

        boolean canWithdraw = accountService.canWithdraw(id, amount);
        String message = canWithdraw ? "Withdrawal amount is available" : "Insufficient funds for withdrawal";
        return ResponseEntity.ok(ApiResponse.success(canWithdraw, message));
    }


}