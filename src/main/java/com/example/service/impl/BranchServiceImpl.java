package com.example.service.impl;

import com.example.config.IbanConfig;
import com.example.dto.request.address.AddressCreateRequest;
import com.example.dto.request.branch.BranchCreateRequest;
import com.example.dto.request.branch.BranchUpdateRequest;
import com.example.dto.response.address.AddressResponse;
import com.example.dto.response.branch.BranchDetailResponse;
import com.example.dto.response.branch.BranchResponse;
import com.example.dto.response.customer.CustomerResponse;
import com.example.entity.Address;
import com.example.entity.Branch;
import com.example.entity.Customer;
import com.example.entity.Institute;
import com.example.mapper.AddressMapper;
import com.example.mapper.BranchMapper;
import com.example.mapper.CustomerMapper;
import com.example.repository.AddressRepository;
import com.example.repository.BranchRepository;
import com.example.repository.InstituteRepository;
import com.example.service.BranchService;
import com.example.util.enums.AddressType;
import com.example.exception.BusinessException;
import com.example.exception.domain.address.AddressNotFoundException;
import com.example.exception.domain.branch.BranchAlreadyExistsException;
import com.example.exception.domain.branch.BranchNotFoundException;
import com.example.exception.domain.institute.InstituteNotFoundException;
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

    public Page<BranchResponse> getAllBranches(Pageable pageable) {
        Page<Branch> branchPage = branchRepository.findAll(pageable);
        return branchPage.map(branchMapper::toBranchResponse);
    }

    public BranchResponse getBranchById(Long id) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException(id));
        return branchMapper.toBranchResponse(branch);
    }

    public BranchDetailResponse createBranch(BranchCreateRequest request) {
        validateBranchCreationInput(request);
        Institute institute = instituteRepository.findById(request.getInstituteId())
                .orElseThrow(() -> new InstituteNotFoundException(request.getInstituteId()));
        Address address = createAndSaveAddress(request.getAddress());

        String bankCode = institute.getBankCode();
        // String countryCode = institute.getAddress().getCountryCode();
        String countryCode = ibanConfig.getCountryCode();
        String locationCode = request.getLocationCode();
        String branchCode = request.getBranchCode() != null ? request.getBranchCode() : "XXX";
        String generatedBicCode = bankCode + countryCode + locationCode + branchCode;

        Branch branch = branchMapper.toBranch(request);
        branch.setInstitute(institute);
        branch.setAddress(address);
        branch.setBicCode(generatedBicCode);
        Branch savedBranch = branchRepository.save(branch);
        return branchMapper.toBranchDetailResponse(savedBranch);
    }

    private void validateBranchCreationInput(BranchCreateRequest branchCreateRequest) {
        if (branchRepository.findByName(branchCreateRequest.getName()).isPresent()) {
            throw new BranchAlreadyExistsException(branchCreateRequest.getName());
        }
        if (branchCreateRequest.getInstituteId() == null) {
            throw new BusinessException("Institute ID is required when creating a branch", HttpStatus.BAD_REQUEST);
        }
        if (branchCreateRequest.getAddress() == null) {
            throw new BusinessException("Address is required when creating a branch", HttpStatus.BAD_REQUEST);
        }
    }

    private Address createAndSaveAddress(AddressCreateRequest addressCreateRequest) {
        Address address = addressMapper.toAddress(addressCreateRequest);
        address.setAddressType(AddressType.BRANCH);
        return addressRepository.save(address);
    }

    public BranchDetailResponse updateBranch(Long id, BranchUpdateRequest request) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException(id));
        branchMapper.updateBranch(branch, branchMapper.toBranch(request));
        return branchMapper.toBranchDetailResponse(branchRepository.save(branch));
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

    public Set<CustomerResponse> getCustomersByBranchId(Long id) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException(id));
        Set<Customer> customers = branch.getCustomers();
        return customerMapper.toCustomerResponseSet(customers);
    }

    public AddressResponse getAddressByBranchId(Long id) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException(id));
        Address address = branch.getAddress();
        if (address != null) {
            return addressMapper.toAddressResponse(address);
        } else throw new AddressNotFoundException(id);
    }


}