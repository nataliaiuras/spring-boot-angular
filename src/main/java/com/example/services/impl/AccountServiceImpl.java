package com.example.services.impl;

import com.example.dtos.AccountDto;
import com.example.dtos.overview.AccountOverviewDto;
import com.example.dtos.overview.CardOverviewDto;
import com.example.dtos.overview.CustomerOverviewDto;
import com.example.entities.Account;
import com.example.entities.Card;
import com.example.entities.Customer;
import com.example.exceptions.domain.account.AccountNotFoundException;
import com.example.mapers.AccountMapper;
import com.example.mapers.CardMapper;
import com.example.mapers.CustomerMapper;
import com.example.repository.AccountRepository;
import com.example.services.AccountService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CardMapper cardMapper;
    private final CustomerMapper customerMapper;


    public Page<AccountOverviewDto> getAllAccounts(Pageable pageable) {
        Page<Account> accountPage = accountRepository.findAll(pageable);
        return accountPage.map(accountMapper::toAccountOverviewDto);
    }

    public AccountDto createAccount(AccountDto accountDto) {
        return accountMapper.toAccountDto(accountRepository.save(accountMapper.toAccount(accountDto)));
    }

    public AccountOverviewDto getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        return accountMapper.toAccountOverviewDto(account);
    }

    public AccountDto updateAccount(Long id, AccountDto accountDto) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        accountMapper.updateAccount(account, accountMapper.toAccount(accountDto));
        return accountMapper.toAccountDto(accountRepository.save(account));
    }

    public void deleteAccount(Long id) {
        accountRepository.delete(accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id)));
    }

    @Override
    public CardOverviewDto getCardByAccountId(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        Card card = account.getCard();
        return cardMapper.toCardOverviewDto(card);
    }

    @Override
    public CustomerOverviewDto getCustomerByAccountId(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        Customer customer = account.getCustomer();
        return customerMapper.toCustomerOverviewDto(customer);
    }


}