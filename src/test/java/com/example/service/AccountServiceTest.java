package com.example.service;

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
import com.example.exception.BusinessException;
import com.example.exception.domain.account.AccountNotFoundException;
import com.example.mapper.AccountMapper;
import com.example.mapper.CardMapper;
import com.example.mapper.CustomerMapper;
import com.example.repository.AccountRepository;
import com.example.repository.CustomerRepository;
import com.example.service.impl.AccountServiceImpl;
import com.example.util.enums.AccountType;
import com.example.util.enums.CardStatus;
import com.example.util.enums.CurrencyType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private IbanGeneratorService ibanGeneratorService;
    @Mock
    private EntityManager entityManager;
    @Mock
    private Query query;

    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        // AccountServiceImpl has an all-args constructor (Lombok @AllArgsConstructor)
        accountService = new AccountServiceImpl(
                accountRepository,
                accountMapper,
                cardMapper,
                customerMapper,
                ibanGeneratorService,
                customerRepository,
                entityManager
        );
    }

    @Test
    void getAllAccounts_mapsEntitiesToResponses() {
        Account acc = sampleAccount(1L, "000000000001", "IBAN-1", AccountType.DEBIT, CurrencyType.USD, new BigDecimal("100.00"));
        Page<Account> page = new PageImpl<>(List.of(acc));
        when(accountRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        AccountResponse mapped = new AccountResponse(1L, acc.getAccountNumber(), acc.getIbanCode(), acc.getType(), acc.getCurrency(), acc.getBalance(), acc.isActive(), Instant.now().toString());
        when(accountMapper.toAccountResponse(acc)).thenReturn(mapped);

        Page<AccountResponse> result = accountService.getAllAccounts(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals(mapped, result.getContent().get(0));
        verify(accountRepository).findAll(PageRequest.of(0, 10));
        verify(accountMapper).toAccountResponse(acc);
    }

    @Test
    void getAccountById_returnsMappedResponse() {
        Account acc = sampleAccount(5L, "000000000005", "IBAN-5", AccountType.CREDIT, CurrencyType.EUR, BigDecimal.ZERO);
        when(accountRepository.findById(5L)).thenReturn(Optional.of(acc));
        AccountResponse mapped = new AccountResponse(5L, acc.getAccountNumber(), acc.getIbanCode(), acc.getType(), acc.getCurrency(), acc.getBalance(), acc.isActive(), Instant.now().toString());
        when(accountMapper.toAccountResponse(acc)).thenReturn(mapped);

        AccountResponse result = accountService.getAccountById(5L);

        assertEquals(mapped, result);
    }

    @Test
    void findAccountById_throwsWhenNotFound() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.findAccountById(99L));
    }

    @Test
    void createAccount_autoCreatesDebitWhenFirstAccountAndTypeIsNotDebit() {
        // Arrange
        AccountCreateRequest req = new AccountCreateRequest();
        req.setType(AccountType.CREDIT);
        req.setCurrency(CurrencyType.EUR);
        req.setCustomerId(10L);

        Customer customer = new Customer();
        customer.setId(10L);
        // first customer account set empty
        when(customerRepository.findById(10L)).thenReturn(Optional.of(customer));

        // mock sequence generator and IBAN
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(123L, 124L); // two sequential calls (DEBIT then CREDIT)
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);
        when(ibanGeneratorService.generateIban(anyString())).thenAnswer(inv -> "IBAN-" + inv.getArgument(0));

        // Saving accounts
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        // Map to Account and to responses
        when(accountMapper.toAccount(any(AccountCreateRequest.class))).thenAnswer(inv -> new Account());
        AccountDetailResponse detail1 = new AccountDetailResponse(1L, "000000000123", "IBAN-000000000123", AccountType.DEBIT, CurrencyType.EUR, BigDecimal.ZERO, true, null, null, null, null, 1L);
        AccountDetailResponse detail2 = new AccountDetailResponse(2L, "000000000124", "IBAN-000000000124", AccountType.CREDIT, CurrencyType.EUR, BigDecimal.ZERO, true, null, null, null, null, 2L);
        // Return second detail for the actually returned saved account; we will capture and stub based on type
        when(accountMapper.toAccountDetailResponse(any(Account.class))).thenAnswer(inv -> {
            Account a = inv.getArgument(0);
            return a.getType() == AccountType.DEBIT ? detail1 : detail2;
        });

        // Act
        AccountDetailResponse result = accountService.createAccount(req);

        // Assert
        assertEquals(AccountType.CREDIT, result.type());
        // verify that two saves happened (DEBIT auto + requested CREDIT)
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository, times(2)).save(accountCaptor.capture());
        List<Account> savedAccounts = accountCaptor.getAllValues();
        assertEquals(AccountType.DEBIT, savedAccounts.get(0).getType());
        assertEquals(AccountType.CREDIT, savedAccounts.get(1).getType());
        // account added to customer's accounts
        assertEquals(2, customer.getAccounts().size());
    }

    @Test
    void createAccount_throwsBusinessExceptionOnDuplicateTypeAndCurrency() {
        // Arrange existing account on customer
        Customer customer = new Customer();
        customer.setId(20L);
        Account existing = sampleAccount(null, "000000000001", "IBAN-X", AccountType.DEBIT, CurrencyType.USD, BigDecimal.ZERO);
        existing.setCustomer(customer);
        customer.getAccounts().add(existing);

        AccountCreateRequest req = new AccountCreateRequest();
        req.setType(AccountType.DEBIT);
        req.setCurrency(CurrencyType.USD);
        req.setCustomerId(20L);

        when(customerRepository.findById(20L)).thenReturn(Optional.of(customer));

        // Act + Assert
        assertThrows(BusinessException.class, () -> accountService.createAccount(req));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void updateAccount_incrementsVersionAndReturnsDetailResponse() {
        Account acc = sampleAccount(30L, "000000000030", "IBAN-30", AccountType.DEBIT, CurrencyType.USD, new BigDecimal("50"));
        when(accountRepository.findById(30L)).thenReturn(Optional.of(acc));

        AccountUpdateRequest updateReq = new AccountUpdateRequest();
        when(accountMapper.toAccount(updateReq)).thenReturn(new Account());

        when(accountRepository.save(acc)).thenReturn(acc);
        AccountDetailResponse detail = new AccountDetailResponse(30L, acc.getAccountNumber(), acc.getIbanCode(), acc.getType(), acc.getCurrency(), acc.getBalance(), acc.isActive(), null, null, null, null, acc.getVersion());
        when(accountMapper.toAccountDetailResponse(acc)).thenReturn(detail);

        AccountDetailResponse result = accountService.updateAccount(30L, updateReq);

        assertEquals(detail, result);
        assertNotNull(acc.getLastModifiedDate());
        verify(accountMapper).updateAccount(eq(acc), any(Account.class));
        verify(accountRepository).save(acc);
    }

    @Test
    void deleteAccount_deletesEntity() {
        Account acc = sampleAccount(40L, "000000000040", "IBAN-40", AccountType.DEBIT, CurrencyType.USD, BigDecimal.ZERO);
        when(accountRepository.findById(40L)).thenReturn(Optional.of(acc));

        accountService.deleteAccount(40L);

        verify(accountRepository).delete(acc);
    }

    @Test
    void deactivateAccount_disablesAccountAndBlocksCards() {
        Account acc = sampleAccount(10L, "000000000050", "IBAN-50", AccountType.DEBIT, CurrencyType.USD, BigDecimal.ZERO);
        Card card1 = new Card();
        card1.setStatus(CardStatus.ACTIVE);
        Card card2 = new Card();
        card2.setStatus(CardStatus.ACTIVE);
        acc.getCards().addAll(Set.of(card1, card2));

        when(accountRepository.findById(10L)).thenReturn(Optional.of(acc));

        accountService.deactivateAccount(10L);

        assertFalse(acc.isActive());
        assertNotNull(acc.getLastModifiedDate());
        assertEquals(CardStatus.BLOCKED, card1.getStatus());
        assertEquals(CardStatus.BLOCKED, card2.getStatus());
        assertEquals("Account deactivated", card1.getBlockedReason());
        verify(accountRepository).save(acc);
    }

    @Test
    void getCardsByAccountId_returnsMappedFirstCard() {
        Account acc = sampleAccount(60L, "000000000060", "IBAN-60", AccountType.DEBIT, CurrencyType.USD, BigDecimal.ZERO);
        Card card = new Card();
        acc.getCards().add(card);
        when(accountRepository.findById(60L)).thenReturn(Optional.of(acc));

        CardResponse cardResp = new CardResponse(1L, "4111111111111111", "JOHN DOE", null, CardStatus.ACTIVE, "2025-01-01T00:00:00Z");
        when(cardMapper.toCardResponse(card)).thenReturn(cardResp);

        Set<CardResponse> result = accountService.getCardsByAccountId(60L);

        assertEquals(1, result.size());
        assertTrue(result.contains(cardResp));
    }

    @Test
    void getCustomerByAccountId_returnsMappedCustomer() {
        Account acc = sampleAccount(70L, "000000000070", "IBAN-70", AccountType.DEBIT, CurrencyType.USD, BigDecimal.ZERO);
        Customer cust = new Customer();
        cust.setId(77L);
        acc.setCustomer(cust);
        when(accountRepository.findById(70L)).thenReturn(Optional.of(acc));

        CustomerResponse resp = new CustomerResponse(77L, null, null, null, null, null);
        when(customerMapper.toCustomerResponse(cust)).thenReturn(resp);

        CustomerResponse result = accountService.getCustomerByAccountId(70L);
        assertEquals(resp, result);
    }

    @Test
    void getAccountBalance_mapsResponse() {
        Account acc = sampleAccount(80L, "000000000080", "IBAN-80", AccountType.DEBIT, CurrencyType.USD, new BigDecimal("123.45"));
        when(accountRepository.findById(80L)).thenReturn(Optional.of(acc));
        AccountBalanceResponse balance = new AccountBalanceResponse(80L, acc.getAccountNumber(), acc.getBalance(), acc.getCurrency().name(), "2025-01-01T00:00:00Z");
        when(accountMapper.toAccountBalanceResponse(acc)).thenReturn(balance);

        AccountBalanceResponse result = accountService.getAccountBalance(80L);
        assertEquals(balance, result);
    }

    @Test
    void canWithdraw_comparesBalance() {
        Account acc = sampleAccount(90L, "000000000090", "IBAN-90", AccountType.DEBIT, CurrencyType.USD, new BigDecimal("200.00"));
        when(accountRepository.findById(90L)).thenReturn(Optional.of(acc));

        assertTrue(accountService.canWithdraw(90L, new BigDecimal("199.99")));
        assertTrue(accountService.canWithdraw(90L, new BigDecimal("200.00")));
        assertFalse(accountService.canWithdraw(90L, new BigDecimal("200.01")));
    }

    // Helpers
    private static Account sampleAccount(Long id, String number, String iban, AccountType type, CurrencyType currency, BigDecimal balance) {
        Account a = new Account();
        a.setId(id);
        a.setAccountNumber(number);
        a.setIbanCode(iban);
        a.setType(type);
        a.setCurrency(currency);
        a.setBalance(balance);
        a.setActive(true);
        a.setCreatedDate(Instant.now());
        a.setLastModifiedDate(Instant.now());
        a.setVersion(1L);
        a.setCustomer(new Customer());
        return a;
    }
}
