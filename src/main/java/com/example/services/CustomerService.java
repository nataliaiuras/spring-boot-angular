package com.example.services;

import com.example.dtos.CustomerDto;
import com.example.dtos.overview.CustomerOverviewDto;
import com.example.entities.Customer;
import com.example.exceptions.domain.customer.CustomerNotFoundException;
import com.example.mapers.CustomerMapper;
import com.example.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public Page<CustomerOverviewDto> getAllCustomers(Pageable pageable) {
        Page<Customer> cardPage = customerRepository.findAll(pageable);
        return cardPage.map(customerMapper::toCustomerOverviewDto);
    }

    public CustomerDto createCustomer(CustomerDto customerDto) {
        return customerMapper.toCustomerDto(customerRepository.save(customerMapper.toCustomer(customerDto)));
    }

    public CustomerDto getCustomerById(Long id) {
        return customerMapper.toCustomerDto(customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id)));
    }

    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer card = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        customerMapper.updateCustomer(card, customerMapper.toCustomer(customerDto));
        return customerMapper.toCustomerDto(customerRepository.save(card));
    }

    public void deleteCustomer(Long id) {
        customerRepository.delete(customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id)));
    }

    public Set<CustomerDto> getBranchCustomersByBranchId(Long branchId) {
        return null;
    }
}
