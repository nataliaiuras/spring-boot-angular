package com.example.services;

import com.example.dtos.BranchDto;
import com.example.dtos.overview.AddressOverviewDto;
import com.example.dtos.overview.BranchOverviewDto;
import com.example.dtos.overview.CustomerOverviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface BranchService {

    Page<BranchOverviewDto> getAllBranches(Pageable pageable);

    BranchOverviewDto getBranchById(Long id);

    BranchDto createBranch(BranchDto branchDto);

    BranchOverviewDto updateBranch(Long id, BranchDto branchDto);

    void deleteBranch(Long id);

    Set<CustomerOverviewDto> getBranchCustomersByBranchId(Long id);

    AddressOverviewDto getBranchAddressByBranchId(Long id);
}
