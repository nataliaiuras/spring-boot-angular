package com.example.services.impl;

import com.example.config.IbanConfig;
import com.example.models.dtos.address.AddressOverviewDto;
import com.example.models.dtos.address.AddressRequestDto;
import com.example.models.dtos.branch.BranchDto;
import com.example.models.dtos.branch.BranchOverviewDto;
import com.example.models.dtos.branch.BranchRequestDto;
import com.example.models.dtos.customer.CustomerOverviewDto;
import com.example.models.entities.Address;
import com.example.models.entities.Institute;
import com.example.models.entities.Branch;
import com.example.models.entities.Customer;
import com.example.exceptions.BusinessException;
import com.example.exceptions.domain.address.AddressNotFoundException;
import com.example.exceptions.domain.institute.InstituteNotFoundException;
import com.example.exceptions.domain.branch.BranchAlreadyExistsException;
import com.example.exceptions.domain.branch.BranchNotFoundException;
import com.example.utils.enums.AddressType;
import com.example.utils.mapers.AddressMapper;
import com.example.utils.mapers.BranchMapper;
import com.example.utils.mapers.CustomerMapper;
import com.example.repository.AddressRepository;
import com.example.repository.InstituteRepository;
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
    private final InstituteRepository instituteRepository;
    private final BranchMapper branchMapper;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final CustomerMapper customerMapper;
    private final IbanConfig ibanConfig;

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
        Institute institute = instituteRepository.findById(dto.getInstituteId()).orElseThrow(() -> new InstituteNotFoundException(dto.getInstituteId()));
        Address address = createAndSaveAddress(dto.getAddress());

        String bankCode = institute.getBankCode();
       // String countryCode = institute.getAddress().getCountryCode();
        String countryCode = ibanConfig.getCountryCode();
        String locationCode = dto.getLocationCode();
        String branchCode = dto.getBranchCode() != null ? dto.getBranchCode() : "XXX";
        String generatedBicCode = bankCode + countryCode + locationCode + branchCode;

        Branch branch = branchMapper.toBranch(dto);
        branch.setInstitute(institute);
        branch.setAddress(address);
        branch.setBicCode(generatedBicCode);
        Branch savedBranch = branchRepository.save(branch);
        return branchMapper.toBranchDto(savedBranch);
    }

    private void validateBranchCreationInput(BranchRequestDto dto) {
        if (branchRepository.findByName(dto.getName()).isPresent()) {
            throw new BranchAlreadyExistsException(dto.getName());
        }
        if (dto.getInstituteId() == null) {
            throw new BusinessException("Institute ID is required when creating a branch", HttpStatus.BAD_REQUEST);
        }
        if (dto.getAddress() == null) {
            throw new BusinessException("Address is required when creating a branch", HttpStatus.BAD_REQUEST);
        }
    }

    private Address createAndSaveAddress(AddressRequestDto dto) {
        Address address = addressMapper.toAddress(dto);
        address.setAddressType(AddressType.BRANCH);
        return addressRepository.save(address);
    }

    public BranchDto updateBranch(Long id, BranchRequestDto dto) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException(id));
        branchMapper.updateBranch(branch, branchMapper.toBranch(dto));
        return branchMapper.toBranchDto(branchRepository.save(branch));
    }

    public void deleteBranch(Long id) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException(id));
        Institute institute = branch.getInstitute();
        if (institute != null) {
            institute.getBranches().remove(branch);
            instituteRepository.save(institute);
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