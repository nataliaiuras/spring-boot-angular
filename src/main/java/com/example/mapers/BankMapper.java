package com.example.mapers;

import com.example.dtos.bank.BankDto;
import com.example.dtos.bank.BankOverviewDto;
import com.example.dtos.bank.BankRequestDto;
import com.example.dtos.bank.BankUpdateDto;
import com.example.entities.Bank;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BankMapper {

    BankDto toBankDto(Bank bank);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Bank toBank(@Valid BankDto dto);

    Bank toBank(@Valid BankRequestDto dto);

    Bank toBank(@Valid BankUpdateDto dto);

    BankOverviewDto toBankOverviewDto(Bank bank);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void updateBank(@MappingTarget Bank target, Bank source);

}