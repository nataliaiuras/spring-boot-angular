package com.example.controllers;

import com.example.exceptions.response.ApiResponse;
import com.example.models.dtos.account.AccountBalanceDto;
import com.example.models.dtos.account.AccountDto;
import com.example.models.dtos.account.AccountOverviewDto;
import com.example.models.dtos.account.AccountRequestDto;
import com.example.models.dtos.card.CardOverviewDto;
import com.example.models.dtos.customer.CustomerOverviewDto;
import com.example.services.AccountService;
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
    public ResponseEntity<ApiResponse<Page<AccountOverviewDto>>> getAllAccounts(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<AccountOverviewDto> pagedAccounts = accountService.getAllAccounts(pageable);
        return ResponseEntity.ok(ApiResponse.success(pagedAccounts));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<AccountOverviewDto>> getAccount(@PathVariable Long id) {
        AccountOverviewDto accountDto = accountService.getAccountById(id);
        return ResponseEntity.ok(ApiResponse.success(accountDto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AccountDto>> createAccount(@Valid @RequestBody AccountRequestDto dto) {
        AccountDto createdAccount = accountService.createAccount(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdAccount.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(ApiResponse.success(createdAccount, "Account created successfully"));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<AccountDto>> updateAccount(@PathVariable Long id, @Valid @RequestBody AccountRequestDto dto) {
        AccountDto updatedAccountDto = accountService.updateAccount(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updatedAccountDto, "Account updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok(ApiResponse.success("Account deleted successfully"));
    }

    @GetMapping("{id}/card")
    public ResponseEntity<ApiResponse<Set<CardOverviewDto>>> getCards(@PathVariable Long id) {
        Set<CardOverviewDto> cards = accountService.getCardsByAccountId(id);
        return ResponseEntity.ok(ApiResponse.success(cards));
    }

    @GetMapping("{id}/customer")
    public ResponseEntity<ApiResponse<CustomerOverviewDto>> getCustomer(@PathVariable Long id) {
        CustomerOverviewDto dto = accountService.getCustomerByAccountId(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("{id}/balance")
    public ResponseEntity<ApiResponse<AccountBalanceDto>> getAccountBalance(@PathVariable Long id) {
        AccountBalanceDto balanceDto = accountService.getAccountBalance(id);
        return ResponseEntity.ok(ApiResponse.success(balanceDto));
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