package com.example.services;

import com.example.dtos.account.AccountDto;
import com.example.dtos.account.AccountBalanceDto;
import com.example.dtos.account.AccountOverviewDto;
import com.example.dtos.account.AccountRequestDto;
import com.example.dtos.card.CardOverviewDto;
import com.example.dtos.customer.CustomerOverviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface AccountService {

    Page<AccountOverviewDto> getAllAccounts(Pageable pageable);

    AccountOverviewDto getAccountById(Long id);

    AccountDto createAccount(AccountRequestDto dto);

    AccountDto updateAccount(Long id, AccountRequestDto dto);

    void deleteAccount(Long id);

    CardOverviewDto getCardByAccountId(Long id);

    CustomerOverviewDto getCustomerByAccountId(Long id);

    AccountBalanceDto getAccountBalance(Long id);

    BigDecimal getAvailableBalance(Long id);

    boolean canWithdraw(Long id, BigDecimal amount);

}
