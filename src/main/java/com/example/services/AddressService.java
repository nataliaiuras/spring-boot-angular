package com.example.services;

import com.example.models.dtos.address.AddressDto;
import com.example.models.dtos.address.AddressOverviewDto;
import com.example.models.dtos.address.AddressRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressService {

    Page<AddressOverviewDto> getAllAddresses(Pageable pageable);

    AddressOverviewDto getAddressById(Long id);

    AddressDto createAddress(AddressRequestDto dto);

    AddressDto updateAddress(Long id, AddressRequestDto dto);

    void deleteAddress(Long id);

}
