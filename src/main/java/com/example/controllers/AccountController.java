package com.example.controllers;

import com.example.dtos.AccountDto;
import com.example.dtos.overview.AccountOverviewDto;
import com.example.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = {"/", ""})
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Set<AccountOverviewDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.allAccount());
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody AccountDto accountDto) {
        AccountDto createdAccount = accountService.createAccount(accountDto);
        return ResponseEntity.created(URI.create("/" + accountDto.getId())).body(createdAccount);
    }

    @GetMapping("{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long id, @Valid @RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(accountService.updateAccount(id, accountDto));
    }

    @PatchMapping("{id}")
    public ResponseEntity<AccountDto> patchAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(accountService.patchAccount(id, accountDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<AccountDto> deleteAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.deleteAccount(id));
    }

    @PostMapping("/{accountId}/card{cardId}")
    public ResponseEntity<AccountDto> setCardToAccount(@PathVariable Long accountId, @PathVariable Long cardId) {
        AccountDto updatedAccount = accountService.setCardToAccount(accountId, cardId);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{accountId}/card{cardId}")
    public ResponseEntity<AccountDto> removeCardFromAccount(@PathVariable Long accountId, @PathVariable Long cardId) {
        AccountDto updatedAccount = accountService.removeCardFromAccount(accountId, cardId);
        return ResponseEntity.ok(updatedAccount);
    }


}