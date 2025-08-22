package com.example.services;

import com.example.dtos.AddressDto;
import com.example.dtos.overview.AddressOverviewDto;
import com.example.entities.Address;
import com.example.exceptions.domain.address.AddressNotFoundException;
import com.example.mapers.AddressMapper;
import com.example.repository.AddressRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public Page<AddressOverviewDto> getAllAddresses(Pageable pageable) {
        Page<Address> addressPage = addressRepository.findAll(pageable);
        return addressPage.map(addressMapper::toAddressOverviewDto);
    }

    public AddressDto createAddress(AddressDto addressDto) {
        return addressMapper.toAddressDto(addressRepository.save(addressMapper.toAddress(addressDto)));
    }

    public AddressDto getAddressById(Long id) {
        return addressMapper.toAddressDto(addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id)));
    }

    public AddressDto updateAddress(Long id, AddressDto addressDto) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id));
        addressMapper.updateAddress(address, addressMapper.toAddress(addressDto));
        return addressMapper.toAddressDto(addressRepository.save(address));
    }

    public void deleteAddress(Long id) {
        addressRepository.delete(addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id)));
    }

    public AddressDto getAddressByBranchId(Long branchId) {
        return null;
    }
}
