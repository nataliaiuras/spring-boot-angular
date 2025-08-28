package com.example.services;

import com.example.dtos.AddressDto;
import com.example.dtos.overview.AddressOverviewDto;
import com.example.dtos.overview.BranchOverviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface AddressService {

    Page<AddressOverviewDto> getAllAddresses(Pageable pageable);

    AddressOverviewDto getAddressById(Long id);

    AddressDto createAddress(AddressDto addressDto);

    AddressDto updateAddress(Long id, AddressDto addressDto);

    void deleteAddress(Long id);

    BranchOverviewDto getBranchByAddressId(Long id);
}
