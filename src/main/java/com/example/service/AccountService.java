package com.example.service;

import com.example.dto.request.account.AccountCreateRequest;
import com.example.dto.request.account.AccountUpdateRequest;
import com.example.dto.response.account.AccountDetailResponse;
import com.example.dto.response.account.AccountBalanceResponse;
import com.example.dto.response.account.AccountResponse;
import com.example.dto.response.card.CardResponse;
import com.example.dto.response.customer.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Set;

public interface AccountService {

    Page<AccountResponse> getAllAccounts(Pageable pageable);

    AccountResponse getAccountById(Long id);

    AccountDetailResponse createAccount(AccountCreateRequest request);

    AccountDetailResponse updateAccount(Long id, AccountUpdateRequest request);

    void deleteAccount(Long id);

    void deactivateAccount(Long id);

    Set<CardResponse> getCardsByAccountId(Long id);

    CustomerResponse getCustomerByAccountId(Long id);

    AccountBalanceResponse getAccountBalance(Long id);

    boolean canWithdraw(Long id, BigDecimal amount);

}
