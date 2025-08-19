package com.example.services;

import com.example.dtos.BranchDto;
import com.example.dtos.overview.BranchOverviewDto;
import com.example.exceptions.AppException;
import com.example.mapers.BranchMapper;
import com.example.models.Address;
import com.example.models.Bank;
import com.example.models.Branch;
import com.example.models.Customer;
import com.example.repository.AddressRepository;
import com.example.repository.BankRepository;
import com.example.repository.BranchRepository;
import com.example.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class BranchService {

    private final BranchRepository branchRepository;
    private final BankRepository bankRepository;
    private final BranchMapper branchMapper;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    public BranchService(BranchRepository branchRepository, BankRepository bankRepository, BranchMapper branchMapper, CustomerRepository customerRepository, AddressRepository addressRepository) {
        this.branchRepository = branchRepository;
        this.bankRepository = bankRepository;
        this.branchMapper = branchMapper;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    public Set<BranchOverviewDto> allBranches() {
        return branchMapper.toBranchOverviewDtos(new HashSet<>(branchRepository.findAll()));
    }

    public BranchDto createBranch(@Valid BranchDto branchDto) {
        if (branchRepository.findByName(branchDto.getName()).isPresent()) {
            throw new AppException("Branch already exists", HttpStatus.CONFLICT);
        }
        Branch branch = branchMapper.toBranch(branchDto);
        branch.setId(null);
        branch.setBank(null);
        branch.setAddress(null);

        Branch savedBranch = branchRepository.save(branch);
        return branchMapper.toBranchDto(savedBranch);
    }

    public BranchDto getBranch(Long id) {
        return branchMapper.toBranchDto(branchRepository.findById(id).orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND)));
    }

    public BranchDto updateBranch(Long id, @Valid BranchDto branchDto) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND));
        branchMapper.updateBranch(branch, branchMapper.toBranch(branchDto));
        Branch savedBranch = branchRepository.save(branch);
        return branchMapper.toBranchDto(savedBranch);
    }

    public BranchDto patchBranch(Long id, BranchDto branchDto) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND));
        if (branchDto.getName() != null) {
            branch.setName(branchDto.getName());
        }
        if (branchDto.getEmail() != null) {
            branch.setEmail(branchDto.getEmail());
        }
        if (branchDto.getTelephoneNumber() != null) {
            branch.setTelephoneNumber(branchDto.getTelephoneNumber());
        }
        if (branchDto.getBicCode() != null) {
            branch.setBicCode(branchDto.getBicCode());
        }
        return branchMapper.toBranchDto(branchRepository.save(branch));
    }

    public void deleteBranch(Long id) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new AppException("Branch not found", HttpStatus.NOT_FOUND));
        Bank bank = branch.getBank();
        if (bank != null) {
            bank.removeBranch(branch);
            bankRepository.save(bank);
        }
        branchRepository.delete(branch);
    }


    public BranchDto addCustomerToBranch(Long branchId, Long customerId) {
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new AppException("Branch not found", HttpStatus.NOT_FOUND));
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new AppException("Customer not found", HttpStatus.NOT_FOUND));
        if (branch.getCustomers().stream().anyMatch(b -> b.getId().equals(customerId))) {
            throw new AppException("Customer already belongs to this branch", HttpStatus.CONFLICT);
        }
        branch.addCustomer(customer);
        branchRepository.save(branch);
        return branchMapper.toBranchDto(branch);
    }


    public BranchDto removeCustomerFromBranch(Long branchId, Long customerId) {
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new AppException("Branch not found", HttpStatus.NOT_FOUND));
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new AppException("Customer not found", HttpStatus.NOT_FOUND));
        if (!branch.getCustomers().contains(customer)) {
            throw new AppException("Customer does not belong to this branch", HttpStatus.CONFLICT);
        }
        branch.getCustomers().remove(customer);
        branchRepository.save(branch);
        return branchMapper.toBranchDto(branch);
    }


    public BranchDto setAddressToBranch(Long branchId, Long addressId) {
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new AppException("Branch not found", HttpStatus.NOT_FOUND));
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new AppException("Address not found", HttpStatus.NOT_FOUND));
        if (branch.getAddress() != null) {
            throw new AppException("Branch already has an address", HttpStatus.CONFLICT);
        }
        branch.setAddress(address);
        branchRepository.save(branch);
        return branchMapper.toBranchDto(branch);
    }

    public BranchDto removeAddressFromBranch(Long branchId, Long addressId) {
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new AppException("Branch not found", HttpStatus.NOT_FOUND));
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new AppException("Address not found", HttpStatus.NOT_FOUND));
        if (!branch.getAddress().equals(address)) {
            throw new AppException("Address does not belong to this branch", HttpStatus.CONFLICT);
        }
        branch.setAddress(null);
        branchRepository.save(branch);
        return branchMapper.toBranchDto(branch);
    }

}