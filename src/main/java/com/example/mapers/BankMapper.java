package com.example.mapers;

import com.example.dtos.BankDto;
import com.example.dtos.overview.BankOverviewDto;
import com.example.models.Bank;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BankMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Bank toBank(BankDto bankDto);

    BankDto toBankDto(Bank bank);

    List<BankDto> toBankDtos(List<Bank> banks);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void updateBank(@MappingTarget Bank target, Bank source);

    Bank fromOverviewToBank(BankOverviewDto bankOverviewDto);

    BankOverviewDto toBankOverviewDto(Bank bank);

    List<BankOverviewDto> toBankOverviewDtos(List<Bank> banks);

}