package com.example.service.impl;

import com.example.config.service.IbanGeneratorService;
import com.example.dto.request.account.AccountCreateRequest;
import com.example.dto.request.account.AccountUpdateRequest;
import com.example.dto.response.account.AccountBalanceResponse;
import com.example.dto.response.account.AccountDetailResponse;
import com.example.dto.response.account.AccountResponse;
import com.example.dto.response.card.CardResponse;
import com.example.dto.response.customer.CustomerResponse;
import com.example.entity.Account;
import com.example.entity.Card;
import com.example.entity.Customer;
import com.example.mapper.AccountMapper;
import com.example.mapper.CardMapper;
import com.example.mapper.CustomerMapper;
import com.example.repository.AccountRepository;
import com.example.repository.CustomerRepository;

import com.example.service.AccountService;
import com.example.util.enums.AccountType;
import com.example.util.enums.CardStatus;
import com.example.exception.BusinessException;
import com.example.exception.domain.account.AccountNotFoundException;
import com.example.exception.domain.customer.CustomerNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CardMapper cardMapper;
    private final CustomerMapper customerMapper;
    private final IbanGeneratorService ibanGenerator;
    private final CustomerRepository customerRepository;
    @PersistenceContext
    private EntityManager entityManager;


    public Page<AccountResponse> getAllAccounts(Pageable pageable) {
        Page<Account> accountPage = accountRepository.findAll(pageable);
        return accountPage.map(accountMapper::toAccountResponse);
    }

    public AccountDetailResponse createAccount(AccountCreateRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.getCustomerId()));
        if (customer.getAccounts().isEmpty() && request.getType() != AccountType.DEBIT) {
            log.info("Customer has no accounts, but account type is not DEBIT, so creating account for customer with ID: {}", request.getCustomerId());
            generateAccount(request, customer, AccountType.DEBIT);
            log.info("AccountResponse created for customer with ID: {}", request.getCustomerId());
        }
        if (!validateRequest(request, customer)) {
            throw new BusinessException("AccountResponse with the same type and currency already exists");
        }
        AccountDetailResponse createdAccount = generateAccount(request, customer, request.getType());
        log.info("AccountResponse of type {} created for customer {}", request.getType(), request.getCustomerId());
        return createdAccount;
    }

    private AccountDetailResponse generateAccount(AccountCreateRequest request, Customer customer, AccountType accountType) {
        Account account = accountMapper.toAccount(request);
        account.setType(accountType);
        String accountNumber = generateAccountNumber();
        account.setAccountNumber(accountNumber);
        String iban = ibanGenerator.generateIban(accountNumber);
        account.setIbanCode(iban);
        account.setBalance(BigDecimal.ZERO);
        account.setActive(true);
        account.setCustomer(customer);
        customer.getAccounts().add(account);
        return accountMapper.toAccountDetailResponse(accountRepository.save(account));
    }

    private boolean validateRequest(AccountCreateRequest accountCreateRequest, Customer customer) {
        return customer.getAccounts().stream()
                .noneMatch(account -> account.getType() == accountCreateRequest.getType() &&
                        account.getCurrency() == accountCreateRequest.getCurrency());
    }

    private String generateAccountNumber() {
        Long nextVal = (Long) entityManager
                .createNativeQuery("SELECT NEXTVAL('account_number_seq')")
                .getSingleResult();
        if (accountRepository.existsByAccountNumber(nextVal.toString())) {
            nextVal = (Long) entityManager
                    .createNativeQuery("SELECT NEXTVAL('account_number_seq')")
                    .getSingleResult();
        }
        return String.format("%012d", nextVal);
    }

    public Account findAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    public AccountResponse getAccountById(Long id) {
        Account account = findAccountById(id);
        return accountMapper.toAccountResponse(account);
    }

    public AccountDetailResponse updateAccount(Long id, AccountUpdateRequest request) {
        Account account = findAccountById(id);
        accountMapper.updateAccount(account, accountMapper.toAccount(request));
        Account saved = accountRepository.save(account);
        return accountMapper.toAccountDetailResponse(saved);
    }

    public void deleteAccount(Long id) {
        accountRepository.delete(findAccountById(id));
    }

    public void deactivateAccount(Long id) {
        Account account = findAccountById(id);
        account.setActive(false);
        accountRepository.save(account);
        if (account.getCards().isEmpty()) {
            return;
        }
        for (Card card : account.getCards()) {
            card.setStatus(CardStatus.BLOCKED);
            card.setBlockedReason("Account deactivated");
        }
    }

    @Override
    public Set<CardResponse> getCardsByAccountId(Long id) {
        Account account = findAccountById(id);
        Set<Card> cards = account.getCards();
        Set<CardResponse> cardResponses = new HashSet<>();
        if (!cards.isEmpty()) {
            cardResponses.add(cardMapper.toCardResponse(cards.iterator().next()));
        }
        return cardResponses;
    }

    @Override
    public CustomerResponse getCustomerByAccountId(Long id) {
        Account account = findAccountById(id);
        Customer customer = account.getCustomer();
        return customerMapper.toCustomerResponse(customer);
    }

    @Override
    public AccountBalanceResponse getAccountBalance(Long id) {
        Account account = findAccountById(id);
        return accountMapper.toAccountBalanceResponse(account);
    }

    @Override
    public boolean canWithdraw(Long id, BigDecimal amount) {
        Account account = findAccountById(id);
        BigDecimal availableBalance = account.getBalance();
        return availableBalance.compareTo(amount) >= 0;
    }


}