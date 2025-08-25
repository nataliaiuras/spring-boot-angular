package com.example.services.impl;

import com.example.dtos.AccountDto;
import com.example.dtos.overview.AccountOverviewDto;
import com.example.entities.Account;
import com.example.exceptions.domain.account.AccountNotFoundException;
import com.example.mapers.AccountMapper;
import com.example.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;


    public Page<AccountOverviewDto> getAllAccounts(Pageable pageable) {
        Page<Account> accountPage = accountRepository.findAll(pageable);
        return accountPage.map(accountMapper::toAccountOverviewDto);
    }

    public AccountDto createAccount(AccountDto accountDto) {
        return accountMapper.toAccountDto(accountRepository.save(accountMapper.toAccount(accountDto)));
    }

    public AccountDto getAccountById(Long id) {
        return accountMapper.toAccountDto(accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id)));
    }

    public AccountDto updateAccount(Long id, AccountDto accountDto) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        accountMapper.updateAccount(account, accountMapper.toAccount(accountDto));
        return accountMapper.toAccountDto(accountRepository.save(account));
    }

    public void deleteAccount(Long id) {
        accountRepository.delete(accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id)));
    }


}