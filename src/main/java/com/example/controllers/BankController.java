package com.example.controllers;

import com.example.dtos.BankDto;
import com.example.dtos.response.PostResponseDto;
import com.example.services.BankService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/banks")
public class BankController {
    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping(value = {"/", ""})
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<BankDto>> getAllBanks() {
        return ResponseEntity.ok(bankService.allBank());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BankDto> createBank(@Valid @RequestBody BankDto bankDto) {
        BankDto createdBank = bankService.createBank(bankDto);
        return ResponseEntity.created(URI.create("/" + bankDto.getId())).body(createdBank);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BankDto> getBank(@PathVariable Long id) {
        return ResponseEntity.ok(bankService.getBank(id));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BankDto> updateBank(@PathVariable Long id, @Valid @RequestBody BankDto bankDto) {
        return ResponseEntity.ok(bankService.updateBank(id, bankDto));
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BankDto> patchBank(@PathVariable Long id, @RequestBody BankDto bankDto) {
        return ResponseEntity.ok(bankService.patchBank(id, bankDto));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BankDto> deleteBank(@PathVariable Long id) {
        return ResponseEntity.ok(bankService.deleteBank(id));
    }
}
