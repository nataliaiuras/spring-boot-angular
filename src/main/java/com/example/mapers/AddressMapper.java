package com.example.mapers;

import com.example.dtos.address.AddressDto;
import com.example.dtos.address.AddressOverviewDto;
import com.example.dtos.address.AddressRequestDto;
import com.example.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Address toAddress(AddressDto addressDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void updateAddress(@MappingTarget Address target, Address source);

    AddressDto toAddressDto(Address address);

    AddressOverviewDto toAddressOverviewDto(Address address);


    Address toAddress(AddressOverviewDto addressOverviewDto);

    Address toAddress(AddressRequestDto dto);
}