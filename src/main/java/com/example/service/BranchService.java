package com.example.service;

import com.example.dto.request.branch.BranchCreateRequest;
import com.example.dto.request.branch.BranchUpdateRequest;
import com.example.dto.response.address.AddressResponse;
import com.example.dto.response.branch.BranchDetailResponse;
import com.example.dto.response.branch.BranchResponse;
import com.example.dto.response.customer.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface BranchService {

    Page<BranchResponse> getAllBranches(Pageable pageable);

    BranchResponse getBranchById(Long id);

    BranchDetailResponse createBranch(BranchCreateRequest request);

    BranchDetailResponse updateBranch(Long id, BranchUpdateRequest request);

    void deleteBranch(Long id);

    Set<CustomerResponse> getCustomersByBranchId(Long id);

    AddressResponse getAddressByBranchId(Long id);

}
