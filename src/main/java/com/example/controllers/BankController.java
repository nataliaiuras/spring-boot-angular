package com.example.controllers;

import com.example.dtos.BankDto;
import com.example.dtos.overview.BankOverviewDto;
import com.example.dtos.overview.BranchOverviewDto;
import com.example.dtos.response.ApiResponse;
import com.example.services.BankService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/banks")
@Validated
@RequiredArgsConstructor
@Slf4j
public class BankController {

    private final BankService bankService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BankOverviewDto>>> getAllBanks(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<BankOverviewDto> pagedBanks = bankService.getAllBanks(pageable);
        return ResponseEntity.ok(ApiResponse.success(pagedBanks));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<BankOverviewDto>> getBank(@PathVariable Long id) {
        BankOverviewDto bankDto = bankService.getBankById(id);
        return ResponseEntity.ok(ApiResponse.success(bankDto));
    }

    @GetMapping("{id}/branches")
    public ResponseEntity<ApiResponse<Set<BranchOverviewDto>>> getBranches(@PathVariable Long id) {
        Set<BranchOverviewDto> branches = bankService.getBranchesByBankId(id);
        return ResponseEntity.ok(ApiResponse.success(branches));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BankDto>> createBank(@Valid @RequestBody BankDto bankDto) {
        BankDto createdBankDto = bankService.createBank(bankDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdBankDto.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(ApiResponse.success(createdBankDto, "Bank created successfully"));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<BankOverviewDto>> updateBank(@PathVariable Long id,
                                                                   @Valid @RequestBody BankDto bankDto) {
        BankOverviewDto updatedBankDto = bankService.updateBank(id, bankDto);
        return ResponseEntity.ok(ApiResponse.success(updatedBankDto, "Bank updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteBank(@PathVariable Long id) {
        bankService.deleteBank(id);
        return ResponseEntity.ok(ApiResponse.success("Bank deleted successfully"));
    }


}
