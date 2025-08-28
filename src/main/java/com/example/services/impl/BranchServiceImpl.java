package com.example.services.impl;

import com.example.dtos.BranchDto;
import com.example.dtos.overview.AddressOverviewDto;
import com.example.dtos.overview.BranchOverviewDto;
import com.example.dtos.overview.CustomerOverviewDto;
import com.example.entities.Address;
import com.example.entities.Bank;
import com.example.entities.Branch;
import com.example.entities.Customer;
import com.example.exceptions.BusinessException;
import com.example.exceptions.domain.address.AddressIsAlreadyAssignedException;
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
        Branch branch= branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException(id));
        return branchMapper.toBranchOverviewDto(branch);
    }

    public BranchDto createBranch(BranchDto branchDto) {
        validateBranchCreationInput(branchDto);
        Bank bank = bankRepository.findById(branchDto.getBank().id())
                .orElseThrow(() -> new BankNotFoundException(branchDto.getBank().id()));
        Address address = createAndSaveAddress(branchDto.getAddress());
        Branch branch = branchMapper.toBranch(branchDto);
        branch.setBank(bank);
        branch.setAddress(address);
        Branch savedBranch = branchRepository.save(branch);
        return branchMapper.toBranchDto(savedBranch);
    }

    private void validateBranchCreationInput(BranchDto branchDto) {
        if (branchRepository.findByName(branchDto.getName()).isPresent()) {
            throw new BranchAlreadyExistsException(branchDto.getName());
        }
        if (branchDto.getBank() == null || branchDto.getBank().id() == null) {
            throw new BusinessException("Bank ID is required when creating a branch", HttpStatus.BAD_REQUEST);
        }
        if (branchDto.getAddress() == null) {
            throw new BusinessException("Address is required when creating a branch", HttpStatus.BAD_REQUEST);
        }
    }

    private Address createAndSaveAddress(AddressOverviewDto addressOverviewDto) {
        Address address = addressMapper.fromOverviewDtoToAddress(addressOverviewDto);
        return addressRepository.save(address);
    }

    public BranchOverviewDto updateBranch(Long id, BranchDto branchDto) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException(id));
        branchMapper.updateBranch(branch, branchMapper.toBranch(branchDto));
        return branchMapper.toBranchOverviewDto(branchRepository.save(branch));
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

    @Override
    public Set<CustomerOverviewDto> getBranchCustomersByBranchId(Long branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException(branchId));
        Set<Customer> customers = branch.getCustomers();
        return customerMapper.toCustomerOverviewDtoSet(customers);
    }


    public AddressOverviewDto getBranchAddressByBranchId(Long branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new BranchNotFoundException(branchId));
        Address address = branch.getAddress();
        if (address != null) {
            return addressMapper.toAddressOverviewDto(address);
        } else throw new AddressNotFoundException(branchId);
    }


}