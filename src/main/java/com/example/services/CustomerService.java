package com.example.services;

import com.example.dtos.CustomerDto;
import com.example.dtos.overview.AccountOverviewDto;
import com.example.dtos.overview.BranchOverviewDto;
import com.example.dtos.overview.CustomerOverviewDto;
import com.example.dtos.overview.UserOverviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface CustomerService {

    Page<CustomerOverviewDto> getAllCustomers(Pageable pageable);

    CustomerOverviewDto getCustomerById(Long id);

    CustomerDto createCustomer(CustomerDto customerDto);

    CustomerDto updateCustomer(Long id, CustomerDto customerDto);

    void deleteCustomer(Long id);

    Set<CustomerDto> getBranchCustomersByBranchId(Long id);

    Set<AccountOverviewDto> getAccountsByCustomerId(Long id);

    BranchOverviewDto getBranchByCustomerId(Long id);

    UserOverviewDto getUserByCustomerId(Long id);
}
