package com.example.controllers;

import com.example.dtos.CustomerDto;
import com.example.dtos.overview.AccountOverviewDto;
import com.example.dtos.overview.BranchOverviewDto;
import com.example.dtos.overview.CustomerOverviewDto;
import com.example.dtos.overview.UserOverviewDto;
import com.example.dtos.response.ApiResponse;
import com.example.services.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/customers")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CustomerOverviewDto>>> getAllCustomers(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<CustomerOverviewDto> pagedCustomer = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(ApiResponse.success(pagedCustomer));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<CustomerOverviewDto>> getCustomer(@PathVariable Long id) {
        CustomerOverviewDto customerDto = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success(customerDto));
    }

    @GetMapping("{id}/accounts")
    public ResponseEntity<ApiResponse<Set<AccountOverviewDto>>> getAccounts(@PathVariable Long id) {
        Set<AccountOverviewDto> accountOverviewDtoSet = customerService.getAccountsByCustomerId(id);
        return ResponseEntity.ok(ApiResponse.success(accountOverviewDtoSet));
    }

    @GetMapping("{id}/branch")
    public ResponseEntity<ApiResponse<BranchOverviewDto>> getBranch(@PathVariable Long id) {
        BranchOverviewDto branchOverviewDto = customerService.getBranchByCustomerId(id);
        return ResponseEntity.ok(ApiResponse.success(branchOverviewDto));
    }

    @GetMapping("{id}/user")
    public ResponseEntity<ApiResponse<UserOverviewDto>> getUser(@PathVariable Long id) {
        UserOverviewDto userOverviewDto = customerService.getUserByCustomerId(id);
        return ResponseEntity.ok(ApiResponse.success(userOverviewDto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDto>> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        CustomerDto createdCustomer = customerService.createCustomer(customerDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdCustomer.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(ApiResponse.success(createdCustomer, "Customer created successfully"));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<CustomerDto>> updateCustomer(@PathVariable Long id,
                                                                   @Valid @RequestBody CustomerDto customerDto) {
        CustomerDto updateCustomer = customerService.updateCustomer(id, customerDto);
        return ResponseEntity.ok(ApiResponse.success(updateCustomer, "Customer updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Customer  deleted successfully"));
    }


}
