package com.example.mapper;

import com.example.dto.request.customer.CustomerCreateRequest;
import com.example.dto.request.customer.CustomerUpdateRequest;
import com.example.dto.response.customer.CustomerDetailResponse;
import com.example.dto.response.customer.CustomerResponse;
import com.example.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CustomerMapper {


    Set<CustomerResponse> toCustomerResponseSet(Set<Customer> customers);

    CustomerResponse toCustomerResponse(Customer customer);

    CustomerDetailResponse toCustomerDetailResponse(Customer customer);

    Customer toCustomer(CustomerCreateRequest request);

    Customer toCustomer(CustomerUpdateRequest request);

    void updateCustomer(@MappingTarget Customer target, Object source);
}
