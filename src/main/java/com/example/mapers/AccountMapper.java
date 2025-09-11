package com.example.mapers;

import com.example.dtos.account.AccountDto;
import com.example.dtos.account.AccountBalanceDto;
import com.example.dtos.account.AccountOverviewDto;
import com.example.dtos.account.AccountRequestDto;
import com.example.entities.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Account toAccount(AccountDto accountDto);

    AccountDto toAccountDto(Account account);

    AccountDto toAccountDto(AccountRequestDto dto);

    Account toAccount(AccountRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void updateAccount(@MappingTarget Account target, Account source);


    AccountOverviewDto toAccountOverviewDto(Account account);

    Set<AccountOverviewDto> toAccountOverviewDtos(Set<Account> accounts);

    AccountBalanceDto toAccountBalanceDto(Account account);
}