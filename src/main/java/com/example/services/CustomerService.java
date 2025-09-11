package com.example.services;

import com.example.dtos.customer.CustomerDto;
import com.example.dtos.account.AccountOverviewDto;
import com.example.dtos.branch.BranchOverviewDto;
import com.example.dtos.customer.CustomerOverviewDto;
import com.example.dtos.customer.CustomerRequestDto;
import com.example.dtos.user.UserOverviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface CustomerService {

    Page<CustomerOverviewDto> getAllCustomers(Pageable pageable);

    CustomerOverviewDto getCustomerById(Long id);

    CustomerDto createCustomer(CustomerRequestDto dto);

    CustomerDto updateCustomer(Long id, CustomerRequestDto dto);

    void deleteCustomer(Long id);

    Set<AccountOverviewDto> getAccountsByCustomerId(Long id);

    BranchOverviewDto getBranchByCustomerId(Long id);

    UserOverviewDto getUserByCustomerId(Long id);
}
