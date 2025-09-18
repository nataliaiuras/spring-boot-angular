package com.example.utils.mapers;

import com.example.models.dtos.branch.BranchDto;
import com.example.models.dtos.branch.BranchOverviewDto;
import com.example.models.dtos.branch.BranchRequestDto;
import com.example.models.entities.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BranchMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Branch toBranch(BranchDto branchDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Branch toBranch(BranchRequestDto branchDto);

    BranchDto toBranchDto(Branch branch);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void updateBranch(@MappingTarget Branch target, Branch source);

    BranchOverviewDto toBranchOverviewDto(Branch branch);

    Set<BranchOverviewDto> toBranchOverviewDtos(Set<Branch> branches);


}