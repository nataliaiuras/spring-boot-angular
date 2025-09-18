package com.example.services.impl;

import com.example.models.dtos.address.AddressDto;
import com.example.models.dtos.address.AddressOverviewDto;
import com.example.models.dtos.address.AddressRequestDto;
import com.example.models.entities.Address;
import com.example.exceptions.domain.address.AddressNotFoundException;
import com.example.utils.mapers.AddressMapper;
import com.example.repository.AddressRepository;
import com.example.services.AddressService;
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

    public Page<AddressOverviewDto> getAllAddresses(Pageable pageable) {
        Page<Address> addressPage = addressRepository.findAll(pageable);
        return addressPage.map(addressMapper::toAddressOverviewDto);
    }

    public AddressOverviewDto getAddressById(Long id) {
        return addressMapper.toAddressOverviewDto(addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id)));
    }

    public AddressDto createAddress(AddressRequestDto dto) {
        return addressMapper.toAddressDto(addressRepository.save(addressMapper.toAddress(dto)));
    }

    public AddressDto updateAddress(Long id, AddressRequestDto dto) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id));
        addressMapper.updateAddress(address, addressMapper.toAddress(dto));
        return addressMapper.toAddressDto(addressRepository.save(address));
    }

    public void deleteAddress(Long id) {
        addressRepository.delete(addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id)));
    }


}
