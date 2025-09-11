package com.example.services.impl;

import com.example.dtos.address.AddressOverviewDto;
import com.example.dtos.address.AddressRequestDto;
import com.example.dtos.branch.BranchDto;
import com.example.dtos.branch.BranchOverviewDto;
import com.example.dtos.branch.BranchRequestDto;
import com.example.dtos.customer.CustomerOverviewDto;
import com.example.entities.Address;
import com.example.entities.Bank;
import com.example.entities.Branch;
import com.example.entities.Customer;
import com.example.exceptions.BusinessException;
import com.example.exceptions.domain.address.AddressNotFoundException;
import com.example.exceptions.domain.bank.BankNotFoundException;
import com.example.exceptions.domain.branch.BranchAlreadyExistsException;
import com.example.exceptions.domain.branch.BranchNotFoundException;
import com.example.mapers.AddressMapper;
import com.example.mapers.BranchMapper;
import com.example.mapers.CustomerMapper;
import com.example.repository.AddressRepository;
import com.example.repository.BankRepository;
import com.example.repository.BranchRepository;
import com.example.services.BranchService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final BankRepository bankRepository;
    private final BranchMapper branchMapper;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final CustomerMapper customerMapper;

    public Page<BranchOverviewDto> getAllBranches(Pageable pageable) {
        Page<Branch> branchPage = branchRepository.findAll(pageable);
        return branchPage.map(branchMapper::toBranchOverviewDto);
    }

    public BranchOverviewDto getBranchById(Long id) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException(id));
        return branchMapper.toBranchOverviewDto(branch);
    }

    public BranchDto createBranch(BranchRequestDto dto) {
        validateBranchCreationInput(dto);
        Bank bank = bankRepository.findById(dto.getBankId()).orElseThrow(() -> new BankNotFoundException(dto.getBankId()));
        Address address = createAndSaveAddress(dto.getAddress());

        String bankCode = bank.getBankCode();
        String countryCode = bank.getAddress().getCountryCode();
        String locationCode = dto.getLocationCode();
        String branchCode = dto.getBranchCode() != null ? dto.getBranchCode() : "XXX";
        String generatedBicCode = bankCode + countryCode + locationCode + branchCode;

        Branch branch = branchMapper.toBranch(dto);
        branch.setBank(bank);
        branch.setAddress(address);
        branch.setBicCode(generatedBicCode);
        Branch savedBranch = branchRepository.save(branch);
        return branchMapper.toBranchDto(savedBranch);
    }

    private void validateBranchCreationInput(BranchRequestDto dto) {
        if (branchRepository.findByName(dto.getName()).isPresent()) {
            throw new BranchAlreadyExistsException(dto.getName());
        }
        if (dto.getBankId() == null) {
            throw new BusinessException("Bank ID is required when creating a branch", HttpStatus.BAD_REQUEST);
        }
        if (dto.getAddress() == null) {
            throw new BusinessException("Address is required when creating a branch", HttpStatus.BAD_REQUEST);
        }
    }

    private Address createAndSaveAddress(AddressRequestDto dto) {
        Address address = addressMapper.toAddress(dto);
        return addressRepository.save(address);
    }

    public BranchDto updateBranch(Long id, BranchRequestDto dto) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException(id));
        branchMapper.updateBranch(branch, branchMapper.toBranch(dto));
        return branchMapper.toBranchDto(branchRepository.save(branch));
    }

    public void deleteBranch(Long id) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException(id));
        Bank bank = branch.getBank();
        if (bank != null) {
            bank.getBranches().remove(branch);
            bankRepository.save(bank);
        }
        branchRepository.delete(branch);
    }

    public Set<CustomerOverviewDto> getCustomersByBranchId(Long id) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException(id));
        Set<Customer> customers = branch.getCustomers();
        return customerMapper.toOverviewDtos(customers);
    }

    public AddressOverviewDto getAddressByBranchId(Long id) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException(id));
        Address address = branch.getAddress();
        if (address != null) {
            return addressMapper.toAddressOverviewDto(address);
        } else throw new AddressNotFoundException(id);
    }


}