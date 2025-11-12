package com.example.mapper;

import com.example.entity.Address;
import com.example.dto.request.address.AddressCreateRequest;
import com.example.dto.request.address.AddressUpdateRequest;
import com.example.dto.response.address.AddressDetailResponse;
import com.example.dto.response.address.AddressResponse;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {


    AddressResponse toAddressResponse(Address address);

    Address toAddress(AddressCreateRequest request);

    Address toAddress(AddressUpdateRequest request);

    AddressDetailResponse toAddressDetailResponse(Address save);

    void updateAddress(@MappingTarget Address target, Address source);
}