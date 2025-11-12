package com.example.service;

import com.example.dto.request.customer.CustomerCreateRequest;
import com.example.dto.request.customer.CustomerUpdateRequest;
import com.example.dto.response.customer.CustomerDetailResponse;
import com.example.dto.response.account.AccountResponse;
import com.example.dto.response.branch.BranchResponse;
import com.example.dto.response.customer.CustomerResponse;
import com.example.dto.response.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface CustomerService {

    Page<CustomerResponse> getAllCustomers(Pageable pageable);

    CustomerResponse getCustomerById(Long id);

    CustomerDetailResponse createCustomer(CustomerCreateRequest request);

    CustomerDetailResponse updateCustomer(Long id, CustomerUpdateRequest request);

    void deleteCustomer(Long id);

    CustomerDetailResponse deactivateCustomer(Long id);

    CustomerDetailResponse reactivateCustomer(Long id);

    Set<AccountResponse> getAccountsByCustomerId(Long id);

    BranchResponse getBranchByCustomerId(Long id);

    UserResponse getUserByCustomerId(Long id);

}
