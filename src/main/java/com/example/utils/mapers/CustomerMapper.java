package com.example.utils.mapers;

import com.example.models.dtos.customer.CustomerDto;
import com.example.models.dtos.customer.CustomerOverviewDto;
import com.example.models.dtos.customer.CustomerRequestDto;
import com.example.models.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Customer toCustomer(CustomerRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void updateCustomer(@MappingTarget Customer target, Customer source);

    CustomerDto toCustomerDto(Customer customer);

    @Mapping(target = "id", ignore = true)
    /*@Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)*/
    CustomerOverviewDto toOverviewDto(Customer customer);


    CustomerRequestDto toRequestDto(Customer customer);

    Set<CustomerOverviewDto> toOverviewDtos(Set<Customer> customers);
}
