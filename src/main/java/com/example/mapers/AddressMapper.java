package com.example.mapers;

import com.example.dtos.AddressDto;
import com.example.models.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toAddress(AddressDto addressDto);

    AddressDto toAddressDto(Address address);

    List<AddressDto> toAddressDtos(List<Address> addresses);

    void updateAddress(@MappingTarget Address target, Address source);
}