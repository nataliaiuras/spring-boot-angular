package com.example.services.impl;

import com.example.config.service.IbanGeneratorService;
import com.example.models.dtos.account.AccountBalanceDto;
import com.example.models.dtos.account.AccountDto;
import com.example.models.dtos.account.AccountOverviewDto;
import com.example.models.dtos.account.AccountRequestDto;
import com.example.models.dtos.card.CardOverviewDto;
import com.example.models.dtos.customer.CustomerOverviewDto;
import com.example.models.entities.Account;
import com.example.models.entities.Card;
import com.example.models.entities.Customer;
import com.example.exceptions.domain.account.AccountNotFoundException;
import com.example.utils.mapers.AccountMapper;
import com.example.utils.mapers.CardMapper;
import com.example.utils.mapers.CustomerMapper;
import com.example.repository.AccountRepository;
import com.example.services.AccountService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CardMapper cardMapper;
    private final CustomerMapper customerMapper;
    private final IbanGeneratorService ibanGenerator;
    @PersistenceContext
    private EntityManager entityManager;


    public Page<AccountOverviewDto> getAllAccounts(Pageable pageable) {
        Page<Account> accountPage = accountRepository.findAll(pageable);
        return accountPage.map(accountMapper::toAccountOverviewDto);
    }

    public AccountDto createAccount(AccountRequestDto dto) {
        Account account = accountMapper.toAccount(dto);

        String accountNumber = generateAccountNumber();
        account.setAccountNumber(accountNumber);

        String iban = ibanGenerator.generateIban(accountNumber);
        account.setIbanCode(iban);

        account.setBalance(BigDecimal.ZERO);
        account.setActive(true);

        Account savedAccount = accountRepository.save(account);
        return accountMapper.toAccountDto(savedAccount);
    }

    private String generateAccountNumber() {
        BigInteger nextVal = (BigInteger) entityManager
                .createNativeQuery("SELECT NEXTVAL('account_number_seq')")
                .getSingleResult();

        return String.format("%012d", nextVal.longValue());
    }

    public Account findAccountById(Long id) {
        return accountRepository.findById(id)
                        .orElseThrow(() -> new AccountNotFoundException(id));
    }

    public AccountOverviewDto getAccountById(Long id) {
        Account account = findAccountById(id);
        return accountMapper.toAccountOverviewDto(account);
    }

    public AccountDto updateAccount(Long id, AccountRequestDto dto) {
        Account account = findAccountById(id);
        accountMapper.updateAccount(account, accountMapper.toAccount(dto));
        Account saved = accountRepository.save(account);
        return accountMapper.toAccountDto(saved);
    }

    public void deleteAccount(Long id) {
        accountRepository.delete(findAccountById(id));
    }

    @Override
    public CardOverviewDto getCardByAccountId(Long id) {
        Account account = findAccountById(id);
        Card card = account.getCard();
        return cardMapper.toCardOverviewDto(card);
    }

    @Override
    public CustomerOverviewDto getCustomerByAccountId(Long id) {
        Account account = findAccountById(id);
        Customer customer = account.getCustomer();
        return customerMapper.toOverviewDto(customer);
    }

    @Override
    public AccountBalanceDto getAccountBalance(Long id) {
        Account account = findAccountById(id);
        return accountMapper.toAccountBalanceDto(account);
    }

    @Override
    public BigDecimal getAvailableBalance(Long id) {
        Account account = findAccountById(id);
        return account.getBalance();
    }

    @Override
    public boolean canWithdraw(Long id, BigDecimal amount) {
        Account account = findAccountById(id);
        BigDecimal availableBalance = account.getBalance();
        return availableBalance.compareTo(amount) >= 0;
    }


}