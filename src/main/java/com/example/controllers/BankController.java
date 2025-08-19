package com.example.controllers;

import com.example.dtos.BankDto;
import com.example.dtos.overview.BankOverviewDto;
import com.example.services.BankService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/banks")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping(value = {"/", ""})
    public ResponseEntity<Set<BankOverviewDto>> getAllBanks() {
        return ResponseEntity.ok(bankService.allBanks());
    }

    @PostMapping
    public ResponseEntity<BankDto> createBank(@Valid @RequestBody BankDto bankDto) {
        BankDto createdBank = bankService.createBank(bankDto);
        return ResponseEntity.created(URI.create("/" + bankDto.getId())).body(createdBank);
    }

    @GetMapping("{id}")
    public ResponseEntity<BankDto> getBank(@PathVariable Long id) {
        return ResponseEntity.ok(bankService.getBank(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<BankDto> updateBank(@PathVariable Long id, @Valid @RequestBody BankDto bankDto) {
        return ResponseEntity.ok(bankService.updateBank(id, bankDto));
    }

    @PatchMapping("{id}")
    public ResponseEntity<BankDto> patchBank(@PathVariable Long id, @Valid @RequestBody BankDto bankDto) {
        return ResponseEntity.ok(bankService.patchBank(id, bankDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBank(@PathVariable Long id) {
        bankService.deleteBank(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{bankId}/branch/{branchId}")
    public ResponseEntity<BankDto> addBranchToBank(@PathVariable Long bankId, @PathVariable Long branchId) {
        BankDto updatedBank = bankService.addBranchToBank(bankId, branchId);
        return ResponseEntity.ok(updatedBank);
    }

    @DeleteMapping("/{bankId}/branch/{branchId}")
    public ResponseEntity<BankDto> removeBranchFromBank(@PathVariable Long bankId, @PathVariable Long branchId) {
        BankDto updatedBank = bankService.removeBranchFromBank(bankId, branchId);
        return ResponseEntity.ok(updatedBank);
    }


}
