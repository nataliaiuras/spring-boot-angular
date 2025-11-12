package com.example.controller;

import com.example.dto.request.branch.BranchCreateRequest;
import com.example.dto.request.branch.BranchUpdateRequest;
import com.example.dto.response.address.AddressResponse;
import com.example.dto.response.branch.BranchDetailResponse;
import com.example.dto.response.branch.BranchResponse;
import com.example.dto.response.customer.CustomerResponse;
import com.example.service.BranchService;
import com.example.dto.response.general.ApiResponse;
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
    public ResponseEntity<ApiResponse<Page<BranchResponse>>> getAllBranches(@RequestParam(defaultValue = "0") @Min(0) int page, @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<BranchResponse> responsePage = branchService.getAllBranches(pageable);
        return ResponseEntity.ok(ApiResponse.success(responsePage));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<BranchResponse>> getBranch(@PathVariable Long id) {
        BranchResponse response = branchService.getBranchById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BranchDetailResponse>> createBranch(@Valid @RequestBody BranchCreateRequest request) {
        BranchDetailResponse response = branchService.createBranch(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(ApiResponse.success(response, "Branch created successfully"));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<BranchDetailResponse>> updateBranch(@PathVariable Long id, @Valid @RequestBody BranchUpdateRequest request) {
        BranchDetailResponse response = branchService.updateBranch(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Branch updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.ok(ApiResponse.success("Branch deleted successfully"));
    }

    @GetMapping("{id}/customers")
    public ResponseEntity<ApiResponse<Set<CustomerResponse>>> getCustomers(@PathVariable Long id) {
        Set<CustomerResponse> responseSet = branchService.getCustomersByBranchId(id);
        return ResponseEntity.ok(ApiResponse.success(responseSet));
    }

    @GetMapping("{id}/address")
    public ResponseEntity<ApiResponse<AddressResponse>> getAddress(@PathVariable Long id) {
        AddressResponse response = branchService.getAddressByBranchId(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


}