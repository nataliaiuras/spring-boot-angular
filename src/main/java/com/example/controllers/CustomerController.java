package com.example.controllers;

import com.example.dtos.CustomerDto;
import com.example.dtos.overview.CustomerOverviewDto;
import com.example.dtos.response.ApiResponse;
import com.example.services.impl.CustomerService;
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

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/customers")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDto>> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        CustomerDto createdCustomer = customerService.createCustomer(customerDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdCustomer.getId()).toUri();
        return ResponseEntity.created(location).body(ApiResponse.success(createdCustomer, "Customer created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CustomerOverviewDto>>> getAllCustomers(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                                                  @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
                                                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                                                  @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<CustomerOverviewDto> pagedCustomer = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(ApiResponse.success(pagedCustomer));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<CustomerDto>> getCustomer(@PathVariable Long id) {
        CustomerDto customerDto = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success(customerDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<CustomerDto>> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDto customerDto) {
        CustomerDto updateCustomer = customerService.updateCustomer(id, customerDto);
        return ResponseEntity.ok(ApiResponse.success(updateCustomer, "Customer updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Customer  deleted successfully"));
    }


}
