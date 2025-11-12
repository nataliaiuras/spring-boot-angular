package com.example.mapper;

import com.example.entity.Branch;
import com.example.dto.request.branch.BranchCreateRequest;
import com.example.dto.request.branch.BranchUpdateRequest;
import com.example.dto.response.branch.BranchDetailResponse;
import com.example.dto.response.branch.BranchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BranchMapper {


    BranchResponse toBranchResponse(Branch branch);

    Branch toBranch(BranchCreateRequest request);

    Branch toBranch(BranchUpdateRequest request);

    BranchDetailResponse toBranchDetailResponse(Branch branch);

    void updateBranch(@MappingTarget Branch target, Branch source);
}