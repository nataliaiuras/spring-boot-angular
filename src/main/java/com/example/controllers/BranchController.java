package com.example.controllers;

import com.example.models.dtos.branch.BranchDto;
import com.example.models.dtos.branch.BranchRequestDto;
import com.example.models.dtos.address.AddressOverviewDto;
import com.example.models.dtos.branch.BranchOverviewDto;
import com.example.models.dtos.customer.CustomerOverviewDto;
import com.example.exceptions.response.ApiResponse;
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

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<BranchOverviewDto>> getBranch(@PathVariable Long id) {
        BranchOverviewDto branchDto = branchService.getBranchById(id);
        return ResponseEntity.ok(ApiResponse.success(branchDto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BranchDto>> createBranch(@Valid @RequestBody BranchRequestDto dto) {
        BranchDto createdDto = branchService.createBranch(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdDto.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(ApiResponse.success(createdDto, "Branch created successfully"));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<BranchDto>> updateBranch(@PathVariable Long id,
                                                               @Valid @RequestBody BranchRequestDto dto) {
        BranchDto updatedBranch = branchService.updateBranch(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updatedBranch, "Branch updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.ok(ApiResponse.success("Branch deleted successfully"));
    }

    @GetMapping("{id}/customers")
    public ResponseEntity<ApiResponse<Set<CustomerOverviewDto>>> getCustomers(@PathVariable Long id) {
        Set<CustomerOverviewDto> customers = branchService.getCustomersByBranchId(id);
        return ResponseEntity.ok(ApiResponse.success(customers));
    }

    @GetMapping("{id}/address")
    public ResponseEntity<ApiResponse<AddressOverviewDto>> getAddress(@PathVariable Long id) {
        AddressOverviewDto address = branchService.getAddressByBranchId(id);
        return ResponseEntity.ok(ApiResponse.success(address));
    }


}