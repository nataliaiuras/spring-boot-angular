package com.example.mapers;

import com.example.dtos.AccountDto;
import com.example.models.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toAccount(AccountDto accountDto);

    AccountDto toAccountDto(Account account);

    List<AccountDto> toAccountDtos(List<Account> accounts);

    void updateAccount(@MappingTarget Account target, Account source);
}