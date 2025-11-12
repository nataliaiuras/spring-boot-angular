package com.example.controller;

import com.example.dto.request.customer.CustomerCreateRequest;
import com.example.dto.request.customer.CustomerUpdateRequest;
import com.example.dto.response.account.AccountResponse;
import com.example.dto.response.branch.BranchResponse;
import com.example.dto.response.customer.CustomerDetailResponse;
import com.example.dto.response.customer.CustomerResponse;
import com.example.dto.response.user.UserResponse;
import com.example.service.CustomerService;
import com.example.dto.response.general.ApiResponse;
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
    public ResponseEntity<ApiResponse<Page<CustomerResponse>>> getAllCustomers(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<CustomerResponse> dtoPage = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(ApiResponse.success(dtoPage));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomer(@PathVariable Long id) {
        CustomerResponse dto = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDetailResponse>> createCustomer(
            @Valid @RequestBody CustomerCreateRequest request) {
        CustomerDetailResponse response = customerService.createCustomer(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(ApiResponse.success(response, "Customer created successfully"));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<CustomerDetailResponse>> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerUpdateRequest request) {
        CustomerDetailResponse response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Customer updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Customer  deleted successfully"));
    }

    @PutMapping("{id}/deactivate")
    public ResponseEntity<ApiResponse<CustomerDetailResponse>> deactivateCustomer(@PathVariable Long id) {
        CustomerDetailResponse customerDetailResponse = customerService.deactivateCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(customerDetailResponse));
    }

    @PutMapping("{id}/reactivate")
    public ResponseEntity<ApiResponse<CustomerDetailResponse>> reactivateCustomer(@PathVariable Long id) {
        CustomerDetailResponse customerDetailResponse = customerService.reactivateCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(customerDetailResponse));
    }

    @GetMapping("{id}/accounts")
    public ResponseEntity<ApiResponse<Set<AccountResponse>>> getAccounts(@PathVariable Long id) {
        Set<AccountResponse> dtos = customerService.getAccountsByCustomerId(id);
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    @GetMapping("{id}/branch")
    public ResponseEntity<ApiResponse<BranchResponse>> getBranch(@PathVariable Long id) {
        BranchResponse dto = customerService.getBranchByCustomerId(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("{id}/user")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        UserResponse dto = customerService.getUserByCustomerId(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }


}
