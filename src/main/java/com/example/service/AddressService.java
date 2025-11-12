package com.example.service;

import com.example.dto.request.address.AddressCreateRequest;
import com.example.dto.request.address.AddressUpdateRequest;
import com.example.dto.response.address.AddressDetailResponse;
import com.example.dto.response.address.AddressResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressService {

    Page<AddressResponse> getAllAddresses(Pageable pageable);

    AddressResponse getAddressById(Long id);

    AddressDetailResponse createAddress(AddressCreateRequest request);

    AddressDetailResponse updateAddress(Long id, AddressUpdateRequest request);

    void deleteAddress(Long id);

}
