package com.example.controllers;

import com.example.dtos.BranchDto;
import com.example.dtos.overview.AddressOverviewDto;
import com.example.dtos.overview.BranchOverviewDto;
import com.example.dtos.overview.CustomerOverviewDto;
import com.example.dtos.response.ApiResponse;
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
@RequestMapping("/api/branches")
@Validated
@Slf4j
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BranchOverviewDto>>> getAllBranches(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<BranchOverviewDto> branchDto = branchService.getAllBranches(pageable);
        return ResponseEntity.ok(ApiResponse.success(branchDto));
    }

    @GetMapping("{id}/customers")
    public ResponseEntity<ApiResponse<Set<CustomerOverviewDto>>> getBranchCustomers(@PathVariable Long id) {
        Set<CustomerOverviewDto> customers = branchService.getBranchCustomersByBranchId(id);
        return ResponseEntity.ok(ApiResponse.success(customers));
    }

    @GetMapping("{id}/address")
    public ResponseEntity<ApiResponse<AddressOverviewDto>> getBranchAddress(@PathVariable Long id) {
        AddressOverviewDto address = branchService.getBranchAddressByBranchId(id);
        return ResponseEntity.ok(ApiResponse.success(address));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BranchDto>> createBranch(@Valid @RequestBody BranchDto bankDto) {
        BranchDto createdBranchDto = branchService.createBranch(bankDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdBranchDto.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(ApiResponse.success(createdBranchDto, "Branch created successfully"));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<BranchOverviewDto>> getBranch(@PathVariable Long id) {
        BranchOverviewDto branchDto = branchService.getBranchById(id);
        return ResponseEntity.ok(ApiResponse.success(branchDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<BranchOverviewDto>> updateBranch(@PathVariable Long id,
                                                                       @Valid @RequestBody BranchDto branchDto) {
        BranchOverviewDto updatedBranch = branchService.updateBranch(id, branchDto);
        return ResponseEntity.ok(ApiResponse.success(updatedBranch, "Branch updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.ok(ApiResponse.success("Branch deleted successfully"));
    }


}