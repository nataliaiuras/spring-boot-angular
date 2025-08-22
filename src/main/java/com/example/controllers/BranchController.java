package com.example.controllers;

import com.example.dtos.AddressDto;
import com.example.dtos.BranchDto;
import com.example.dtos.CustomerDto;
import com.example.dtos.overview.BranchOverviewDto;
import com.example.dtos.response.ApiResponse;
import com.example.services.AddressService;
import com.example.services.BranchService;
import com.example.services.CustomerService;
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
    private final CustomerService customerService;
    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BranchOverviewDto>>> getAllBranches(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                                               @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
                                                                               @RequestParam(defaultValue = "id") String sortBy,
                                                                               @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<BranchOverviewDto> branchDto = branchService.getAllBranches(pageable);
        return ResponseEntity.ok(ApiResponse.success(branchDto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BranchDto>> createBranch(@Valid @RequestBody BranchDto bankDto) {
        BranchDto createdBranchDto = branchService.createBranch(bankDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdBranchDto.getId()).toUri();
        return ResponseEntity.created(location).body(ApiResponse.success(createdBranchDto, "Bank created successfully"));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<BranchDto>> getBranch(@PathVariable Long id) {
        BranchDto branchDto = branchService.getBranchById(id);
        return ResponseEntity.ok(ApiResponse.success(branchDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<BranchDto>> updateBranch(@PathVariable Long id,
                                                               @Valid @RequestBody BranchDto branchDto) {
        BranchDto updatedBranch = branchService.updateBranch(id, branchDto);
        return ResponseEntity.ok(ApiResponse.success(updatedBranch, "Branch updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.ok(ApiResponse.success("Branch deleted successfully"));
    }

    @GetMapping("{branchId}/customers")
    public ResponseEntity<Set<CustomerDto>> getBranchCustomers(@PathVariable Long branchId) {
        Set<CustomerDto> customers = customerService.getBranchCustomersByBranchId(branchId);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("{branchId}/address")
    public ResponseEntity<AddressDto> getBranchAddress(@PathVariable Long branchId) {
        AddressDto address = addressService.getAddressByBranchId(branchId);
        return ResponseEntity.ok(address);
    }


}