package com.example.controllers;

import com.example.models.dtos.account.AccountOverviewDto;
import com.example.models.dtos.branch.BranchOverviewDto;
import com.example.models.dtos.customer.CustomerDto;
import com.example.models.dtos.customer.CustomerOverviewDto;
import com.example.models.dtos.customer.CustomerRequestDto;
import com.example.models.dtos.user.UserOverviewDto;
import com.example.exceptions.response.ApiResponse;
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
        Page<CustomerOverviewDto> dtoPage = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(ApiResponse.success(dtoPage));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<CustomerOverviewDto>> getCustomer(@PathVariable Long id) {
        CustomerOverviewDto dto = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDto>> createCustomer(@Valid @RequestBody CustomerRequestDto dto) {
        CustomerDto createdDto = customerService.createCustomer(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdDto.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(ApiResponse.success(createdDto, "Customer created successfully"));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<CustomerDto>> updateCustomer(@PathVariable Long id,
                                                                   @Valid @RequestBody CustomerRequestDto dto) {
        CustomerDto updatedDto = customerService.updateCustomer(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updatedDto, "Customer updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Customer  deleted successfully"));
    }

    @GetMapping("{id}/accounts")
    public ResponseEntity<ApiResponse<Set<AccountOverviewDto>>> getAccounts(@PathVariable Long id) {
        Set<AccountOverviewDto> dtos = customerService.getAccountsByCustomerId(id);
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    @GetMapping("{id}/branch")
    public ResponseEntity<ApiResponse<BranchOverviewDto>> getBranch(@PathVariable Long id) {
        BranchOverviewDto dto = customerService.getBranchByCustomerId(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("{id}/user")
    public ResponseEntity<ApiResponse<UserOverviewDto>> getUser(@PathVariable Long id) {
        UserOverviewDto dto = customerService.getUserByCustomerId(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }


}
