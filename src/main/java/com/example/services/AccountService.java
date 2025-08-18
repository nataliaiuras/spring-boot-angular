package com.example.services;

import com.example.dtos.AccountDto;
import com.example.dtos.CustomerDto;
import com.example.dtos.overview.AccountOverviewDto;
import com.example.exceptions.AppException;
import com.example.mapers.AccountMapper;
import com.example.models.Account;
import com.example.repository.AccountRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    public List<AccountOverviewDto> allAccount() {
        return accountMapper.toAccountOverviewDtos(accountRepository.findAll());
    }

    public AccountDto createAccount(@Valid AccountDto accountDto) {
        return accountMapper.toAccountDto(accountRepository.save(accountMapper.toAccount(accountDto)));
    }

    public AccountDto getAccount(Long id) {
        return accountMapper.toAccountDto(accountRepository.findById(id).orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND)));
    }

    public AccountDto updateAccount(Long id, @Valid AccountDto accountDto) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND));
        accountMapper.updateAccount(account, accountMapper.toAccount(accountDto));
        Account savedAccount = accountRepository.save(account);
        return accountMapper.toAccountDto(savedAccount);
    }

    public AccountDto patchAccount(Long id, AccountDto accountDto) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND));

        if (accountDto.getAccountNumber() != null) {
            account.setAccountNumber(accountDto.getAccountNumber());
        }
        if (accountDto.getType() != null){
            account.setType(accountDto.getType());
        }
        if (accountDto.getIbanCode() != null) {
            account.setIbanCode(accountDto.getIbanCode());
        }
        if (accountDto.getBalance().compareTo(account.getBalance()) != 0) {
            account.setBalance(accountDto.getBalance());
        }

        return accountMapper.toAccountDto(accountRepository.save(account));
    }

    public AccountDto deleteAccount(Long id) {
        AccountDto accountDto = getAccount(id);
        accountRepository.deleteById(id);
        return accountDto;
    }

    public CustomerDto getCustomer(Long customerId) {
        return null;
    }

}