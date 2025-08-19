package com.example.mapers;

import com.example.dtos.BankDto;
import com.example.dtos.overview.BankOverviewDto;
import com.example.models.Bank;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BankMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Bank toBank(BankDto bankDto);

    BankDto toBankDto(Bank bank);

    Set<BankDto> toBankDtos(Set<Bank> banks);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void updateBank(@MappingTarget Bank target, Bank source);

    BankOverviewDto toBankOverviewDto(Bank bank);

    Set<BankOverviewDto> toBankOverviewDtos(Set<Bank> banks);


    /*    default Set<BranchOverviewDto> toBranchOverviewDtos(Set<Branch> branches) {
        if (branches == null) {
            return new HashSet<>();
        }
        return branches.stream()
                .map(this::toBranchOverviewDto)
                .collect(Collectors.toSet());
    }*/

}