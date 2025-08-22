package com.example.mapers;

import com.example.dtos.AccountDto;
import com.example.dtos.overview.AccountOverviewDto;
import com.example.entities.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Account toAccount(AccountDto accountDto);

    AccountDto toAccountDto(Account account);

    AccountOverviewDto toAccountOverviewDto(Account account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void updateAccount(@MappingTarget Account target, Account source);


}