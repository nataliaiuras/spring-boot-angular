package com.example.controllers;

import com.example.dtos.BankDto;
import com.example.dtos.BranchDto;
import com.example.dtos.overview.BankOverviewDto;
import com.example.dtos.response.ApiResponse;
import com.example.services.BankService;
import com.example.services.BranchService;
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
    private final BranchService branchService;

    @PostMapping
    public ResponseEntity<ApiResponse<BankDto>> createBank(@Valid @RequestBody BankDto bankDto) {
        BankDto createdBankDto = bankService.createBank(bankDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdBankDto.getId()).toUri();
        return ResponseEntity.created(location).body(ApiResponse.success(createdBankDto, "Bank created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BankOverviewDto>>> getAllBanks(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                                          @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
                                                                          @RequestParam(defaultValue = "id") String sortBy,
                                                                          @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<BankOverviewDto> pagedBanks = bankService.getAllBanks(pageable);
        return ResponseEntity.ok(ApiResponse.success(pagedBanks));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<BankDto>> getBank(@PathVariable Long id) {
        BankDto bankDto = bankService.getBankById(id);
        return ResponseEntity.ok(ApiResponse.success(bankDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<BankDto>> updateBank(@PathVariable Long id, @Valid @RequestBody BankDto bankDto) {
        BankDto updatedBankDto = bankService.updateBank(id, bankDto);
        return ResponseEntity.ok(ApiResponse.success(updatedBankDto, "Bank updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteBank(@PathVariable Long id) {
        bankService.deleteBank(id);
        return ResponseEntity.ok(ApiResponse.success("Bank deleted successfully"));
    }

    @GetMapping("{bankId}/branches")
    public ResponseEntity<ApiResponse<Set<BranchDto>>> getBankBranches(@PathVariable Long bankId) {
        Set<BranchDto> branches = branchService.getBranchesByBankId(bankId);
        return ResponseEntity.ok(ApiResponse.success(branches));
    }


}
