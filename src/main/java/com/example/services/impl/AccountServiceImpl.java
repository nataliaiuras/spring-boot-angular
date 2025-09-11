package com.example.services.impl;

import com.example.dtos.account.AccountDto;
import com.example.dtos.account.AccountBalanceDto;
import com.example.dtos.account.AccountOverviewDto;
import com.example.dtos.account.AccountRequestDto;
import com.example.dtos.card.CardOverviewDto;
import com.example.dtos.customer.CustomerOverviewDto;
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

import java.math.BigDecimal;

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

    public AccountDto createAccount(AccountRequestDto dto) {
        return accountMapper.toAccountDto(accountRepository.save(accountMapper.toAccount(dto)));
    }

    public AccountOverviewDto getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        return accountMapper.toAccountOverviewDto(account);
    }

    public AccountDto updateAccount(Long id, AccountRequestDto dto) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        accountMapper.updateAccount(account, accountMapper.toAccount(dto));
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
        return customerMapper.toOverviewDto(customer);
    }

    @Override
    public AccountBalanceDto getAccountBalance(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        return accountMapper.toAccountBalanceDto(account);
    }

    @Override
    public BigDecimal getAvailableBalance(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        return account.getAvailableBalance();
    }

    @Override
    public boolean canWithdraw(Long id, BigDecimal amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        BigDecimal availableBalance = account.getAvailableBalance();
        return availableBalance.compareTo(amount) >= 0;
    }


}