package com.example.controllers;

import com.example.dtos.AccountDto;
import com.example.dtos.overview.AccountOverviewDto;
import com.example.dtos.response.ApiResponse;
import com.example.services.AccountService;
import jakarta.validation.Valid;
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

import java.net.URI;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountDto>> createAccount(@Valid @RequestBody AccountDto accountDto) {
        AccountDto createdAccount = accountService.createAccount(accountDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdAccount.getId()).toUri();
        return ResponseEntity.created(location).body(ApiResponse.success(createdAccount, "Account created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AccountOverviewDto>>> getAllAccounts(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                                                @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
                                                                                @RequestParam(defaultValue = "id") String sortBy,
                                                                                @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<AccountOverviewDto> pagedAccounts = accountService.getAllAccounts(pageable);
        return ResponseEntity.ok(ApiResponse.success(pagedAccounts));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<AccountDto>> getAccount(@PathVariable Long id) {
        AccountDto accountDto = accountService.getAccountById(id);
        return ResponseEntity.ok(ApiResponse.success(accountDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<AccountDto>> updateAccount(@PathVariable Long id, @Valid @RequestBody AccountDto accountDto) {
        AccountDto updatedAccountDto = accountService.updateAccount(id, accountDto);
        return ResponseEntity.ok(ApiResponse.success(updatedAccountDto, "Account updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok(ApiResponse.success("Account deleted successfully"));
    }


}