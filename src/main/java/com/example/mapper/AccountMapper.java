package com.example.mapper;

import com.example.entity.Account;
import com.example.dto.request.account.AccountCreateRequest;
import com.example.dto.request.account.AccountUpdateRequest;
import com.example.dto.response.account.AccountBalanceResponse;
import com.example.dto.response.account.AccountDetailResponse;
import com.example.dto.response.account.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CardMapper.class})
public interface AccountMapper {

    AccountResponse toAccountResponse(Account account);

    Account toAccount(AccountCreateRequest accountCreateRequest);

    Account toAccount(AccountUpdateRequest accountUpdateRequest);

    AccountDetailResponse toAccountDetailResponse(Account account);

    void updateAccount(@MappingTarget Account target, Account source);

    AccountBalanceResponse toAccountBalanceResponse(Account account);

/*    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void updateAccount(@MappingTarget AccountResponse target, AccountResponse source);

*/
}
