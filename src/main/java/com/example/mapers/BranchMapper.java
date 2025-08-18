package com.example.mapers;

import com.example.dtos.BranchDto;
import com.example.dtos.overview.BranchOverviewDto;
import com.example.models.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    Branch toBranch(BranchDto branchDto);

    BranchDto toBranchDto(Branch branch);

    List<BranchDto> toBranchDtos(List<Branch> branches);

    void updateBranch(@MappingTarget Branch target, Branch source);

    BranchOverviewDto toBranchOverviewDto(Branch branch);

    List<BranchOverviewDto> toBranchOverviewDtos(List<Branch> branches);


}