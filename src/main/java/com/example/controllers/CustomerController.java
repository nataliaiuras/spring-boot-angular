package com.example.controllers;

import com.example.dtos.CustomerDto;
import com.example.dtos.overview.CustomerOverviewDto;
import com.example.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(value = {"/", ""})
    public ResponseEntity<List<CustomerOverviewDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.allCustomers());
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        CustomerDto createdCustomer = customerService.createCustomer(customerDto);
        return ResponseEntity.created(URI.create("/" + customerDto.getId())).body(createdCustomer);
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomer(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customerDto));
    }

    @PatchMapping("{id}")
    public ResponseEntity<CustomerDto> patchCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customerService.patchCustomer(id, customerDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CustomerDto> deleteCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.deleteCustomer(id));
    }
}
