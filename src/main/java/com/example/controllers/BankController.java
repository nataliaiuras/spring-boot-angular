package com.example.controllers;

import com.example.dtos.BankDto;
import com.example.dtos.overview.BankOverviewDto;
import com.example.services.BankService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<BankOverviewDto>> getAllBanks() {
        return ResponseEntity.ok(bankService.getAllBanksOverview());
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
    public ResponseEntity<BankDto> deleteBank(@PathVariable Long id) {
        return ResponseEntity.ok(bankService.deleteBank(id));
    }
}
