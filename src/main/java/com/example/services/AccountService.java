package com.example.services;

import com.example.dtos.AccountDto;
import com.example.dtos.overview.AccountOverviewDto;
import com.example.dtos.overview.CardOverviewDto;
import com.example.dtos.overview.CustomerOverviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {

    Page<AccountOverviewDto> getAllAccounts(Pageable pageable);

    AccountOverviewDto getAccountById(Long id);

    AccountDto createAccount(AccountDto accountDto);

    AccountDto updateAccount(Long id, AccountDto accountDto);

    void deleteAccount(Long id);

    CardOverviewDto getCardByAccountId(Long id);

    CustomerOverviewDto getCustomerByAccountId(Long id);
}
