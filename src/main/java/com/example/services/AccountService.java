package com.example.services;

import com.example.models.dtos.account.AccountDto;
import com.example.models.dtos.account.AccountBalanceDto;
import com.example.models.dtos.account.AccountOverviewDto;
import com.example.models.dtos.account.AccountRequestDto;
import com.example.models.dtos.card.CardOverviewDto;
import com.example.models.dtos.customer.CustomerOverviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Set;

public interface AccountService {

    Page<AccountOverviewDto> getAllAccounts(Pageable pageable);

    AccountOverviewDto getAccountById(Long id);

    AccountDto createAccount(AccountRequestDto dto);

    AccountDto updateAccount(Long id, AccountRequestDto dto);

    void deleteAccount(Long id);

    Set<CardOverviewDto> getCardsByAccountId(Long id);

    CustomerOverviewDto getCustomerByAccountId(Long id);

    AccountBalanceDto getAccountBalance(Long id);

    boolean canWithdraw(Long id, BigDecimal amount);

}
