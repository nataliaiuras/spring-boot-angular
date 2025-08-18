package com.example.services;

import com.example.dtos.AddressDto;
import com.example.exceptions.AppException;
import com.example.mapers.AddressMapper;
import com.example.models.Address;
import com.example.repository.AddressRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AddressService(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    public List<AddressDto> allAddress() {
        return addressMapper.toAddressDtos(addressRepository.findAll());
    }

    public AddressDto createAddress(@Valid AddressDto addressDto) {
        return addressMapper.toAddressDto(addressRepository.save(addressMapper.toAddress(addressDto)));
    }

    public AddressDto getAddress(Long id) {
        return addressMapper.toAddressDto(addressRepository.findById(id).orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND)));
    }

    public AddressDto updateAddress(Long id, @Valid AddressDto addressDto) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND));
        addressMapper.updateAddress(address, addressMapper.toAddress(addressDto));
        Address savedAddress = addressRepository.save(address);
        return addressMapper.toAddressDto(savedAddress);
    }

    public AddressDto patchAddress(Long id, AddressDto addressDto) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND));
        if (addressDto.getStreet() != null) {
            address.setStreet(addressDto.getStreet());
        }
        if (addressDto.getNumber() != 0) {
            address.setNumber(addressDto.getNumber());
        }
        if (addressDto.getCity() != null) {
            address.setCity(addressDto.getCity());
        }
        if (addressDto.getCounty() != null) {
            address.setCounty(addressDto.getCounty());
        }
        if (addressDto.getPostalCode() != null) {
            address.setPostalCode(addressDto.getPostalCode());
        }
        if (addressDto.getCountry() != null) {
            address.setCountry(addressDto.getCountry());
        }
        return addressMapper.toAddressDto(addressRepository.save(address));
    }

    public AddressDto deleteAddress(Long id) {
        AddressDto addressDto = getAddress(id);
        addressRepository.deleteById(id);
        return addressDto;
    }
}
