package com.example.services;

import com.example.models.dtos.customer.CustomerDto;
import com.example.models.dtos.account.AccountOverviewDto;
import com.example.models.dtos.branch.BranchOverviewDto;
import com.example.models.dtos.customer.CustomerOverviewDto;
import com.example.models.dtos.customer.CustomerRequestDto;
import com.example.models.dtos.user.UserOverviewDto;
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
