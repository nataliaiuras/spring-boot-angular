package com.example.services;

import com.example.models.dtos.address.AddressOverviewDto;
import com.example.models.dtos.branch.BranchDto;
import com.example.models.dtos.branch.BranchOverviewDto;
import com.example.models.dtos.branch.BranchRequestDto;
import com.example.models.dtos.customer.CustomerOverviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface BranchService {

    Page<BranchOverviewDto> getAllBranches(Pageable pageable);

    BranchOverviewDto getBranchById(Long id);

    BranchDto createBranch(BranchRequestDto dto);

    BranchDto updateBranch(Long id, BranchRequestDto dto);

    void deleteBranch(Long id);

    Set<CustomerOverviewDto> getCustomersByBranchId(Long id);

    AddressOverviewDto getAddressByBranchId(Long id);
}
