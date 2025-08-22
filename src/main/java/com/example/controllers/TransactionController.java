package com.example.controllers;


import com.example.dtos.TransactionDto;
import com.example.dtos.overview.TransactionOverviewDto;
import com.example.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/transactions")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

 /*   @GetMapping(value = {"/", ""})
    public ResponseEntity<Set<TransactionOverviewDto>> allTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("{id}")
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("account/{accountId}")
    public ResponseEntity<Set<TransactionDto>> getAllTransactionsByAccountId(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getAllTransactionsByAccountId(accountId));
    }*/

 /*   @PostMapping("transfer/{id}")
    public ResponseEntity<TransactionDto> transfer(@PathVariable Long id, @Valid @RequestBody TransactionDto transactionRequestDto) {
        TransactionDto result = transactionService.transfer(id, transactionRequestDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);


    @PostMapping("betweenOwnAccounts")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TransactionDto> transferBetweenOwnAccounts(@Valid @RequestBody TransactionDto transactionDto) {
        TransactionDto result = transactionService.transferBetweenOwnAccounts(transactionDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("betweenOwnBanks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TransactionDto> transferBetweenOwnBank(@Valid @RequestBody TransactionDto transactionDto) {
        TransactionDto result = transactionService.transferBetweenOwnBank(transactionDto.getFromAccount(),
                transactionDto.getToAccount(), transactionDto.getAmount());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("ToDifferentBank")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TransactionDto> transferToDifferentBank(@Valid @RequestBody TransactionDto transactionDto) {
        TransactionDto result = transactionService.transferToDifferentBank(transactionDto.getFromAccount(),
                transactionDto.getToAccount(), transactionDto.getAmount());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }*/


}
