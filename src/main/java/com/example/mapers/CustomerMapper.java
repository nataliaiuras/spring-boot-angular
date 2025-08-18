package com.example.mapers;

import com.example.dtos.CustomerDto;
import com.example.dtos.overview.CustomerOverviewDto;
import com.example.models.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toCustomer(CustomerDto customerDto);

    CustomerDto toCustomerDto(Customer customer);

    List<CustomerDto> toCustomerDtos(List<Customer> customers);

    void updateCustomer(@MappingTarget Customer target, Customer source);

    CustomerOverviewDto toCustomerOverviewDto(Customer customer);

    List<CustomerOverviewDto> toCustomerOverviewDtos(List<Customer> customers);

}
