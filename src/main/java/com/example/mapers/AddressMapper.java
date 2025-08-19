package com.example.mapers;

import com.example.dtos.AddressDto;
import com.example.dtos.overview.AddressOverviewDto;
import com.example.models.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Address toAddress(AddressDto addressDto);

    AddressDto toAddressDto(Address address);

    Set<AddressDto> toAddressDtos(Set<Address> addresses);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void updateAddress(@MappingTarget Address target, Address source);

    AddressOverviewDto toAddressOverviewDto(Address address);

    Set<AddressOverviewDto> toAddressOverviewDtos(Set<Address> addresses);
}