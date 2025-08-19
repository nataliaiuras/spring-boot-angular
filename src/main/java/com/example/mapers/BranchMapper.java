package com.example.mapers;

import com.example.dtos.BranchDto;
import com.example.dtos.overview.BranchOverviewDto;
import com.example.models.Branch;
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

    BranchDto toBranchDto(Branch branch);

    Set<BranchDto> toBranchDtos(Set<Branch> branches);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void updateBranch(@MappingTarget Branch target, Branch source);

    BranchOverviewDto toBranchOverviewDto(Branch branch);

    Set<BranchOverviewDto> toBranchOverviewDtos(Set<Branch> branches);


}