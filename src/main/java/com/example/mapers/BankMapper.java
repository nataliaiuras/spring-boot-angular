package com.example.mapers;

import com.example.dtos.BankDto;
import com.example.models.Bank;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BankMapper {

    Bank toBank(BankDto bankDto);

    BankDto toBankDto(Bank bank);

    List<BankDto> toBankDtos(List<Bank> banks);

    void updateBank(@MappingTarget Bank target, Bank source);
}