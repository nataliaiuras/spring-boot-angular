package com.example.service.impl;

import com.example.dto.request.address.AddressCreateRequest;
import com.example.dto.request.address.AddressUpdateRequest;
import com.example.dto.response.address.AddressDetailResponse;
import com.example.dto.response.address.AddressResponse;

import com.example.mapper.AddressMapper;
import com.example.entity.Address;
import com.example.repository.AddressRepository;

import com.example.exception.domain.address.AddressNotFoundException;
import com.example.service.AddressService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public Page<AddressResponse> getAllAddresses(Pageable pageable) {
        Page<Address> addressPage = addressRepository.findAll(pageable);
        return addressPage.map(addressMapper::toAddressResponse);
    }

    public AddressResponse getAddressById(Long id) {
        return addressMapper.toAddressResponse(addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id)));
    }

    public AddressDetailResponse createAddress(AddressCreateRequest request) {
        return addressMapper.toAddressDetailResponse(addressRepository.save(addressMapper.toAddress(request)));
    }

    public AddressDetailResponse updateAddress(Long id, AddressUpdateRequest request) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id));
        addressMapper.updateAddress(address, addressMapper.toAddress(request));
        return addressMapper.toAddressDetailResponse(addressRepository.save(address));
    }

    public void deleteAddress(Long id) {
        addressRepository.delete(addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id)));
    }


}
