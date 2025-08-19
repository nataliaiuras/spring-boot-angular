package com.example.services;

import com.example.dtos.AccountDto;
import com.example.dtos.overview.AccountOverviewDto;
import com.example.exceptions.AppException;
import com.example.mapers.AccountMapper;
import com.example.models.Account;
import com.example.models.Card;
import com.example.repository.AccountRepository;
import com.example.repository.CardRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository, CardRepository cardRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.accountMapper = accountMapper;
    }

    public Set<AccountOverviewDto> allAccount() {
        return accountMapper.toAccountOverviewDtos(new HashSet<>(accountRepository.findAll()));
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
        if (accountDto.getType() != null) {
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


    public AccountDto setCardToAccount(Long accountId, Long cardId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException("Account not found", HttpStatus.NOT_FOUND));
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new AppException("Card not found", HttpStatus.NOT_FOUND));
        if (card.getAccount() != null) {
            throw new AppException("Card is owned by an account", HttpStatus.CONFLICT);
        }
        account.setCard(card);
        Account savedAccount = accountRepository.save(account);
        return accountMapper.toAccountDto(savedAccount);
    }

    public AccountDto removeCardFromAccount(Long accountId, Long cardId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException("Account not found", HttpStatus.NOT_FOUND));
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new AppException("Card not found", HttpStatus.NOT_FOUND));
        if (!account.getCard().equals(card)) {
            throw new AppException("Card is not owned by this account", HttpStatus.CONFLICT);
        }
        account.setCard(null);
        Account savedAccount = accountRepository.save(account);
        return accountMapper.toAccountDto(savedAccount);
    }
}