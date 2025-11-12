package com.example.service.impl;

import com.example.dto.response.account.AccountResponse;
import com.example.dto.response.card.CardDetailResponse;
import com.example.dto.response.card.CardResponse;
import com.example.dto.response.customer.CustomerResponse;
import com.example.entity.Account;
import com.example.entity.Card;
import com.example.exception.BusinessException;
import com.example.exception.domain.account.AccountNotFoundException;
import com.example.exception.domain.card.CardAlreadyExistsException;
import com.example.exception.domain.card.CardNotFoundException;
import com.example.mapper.AccountMapper;
import com.example.mapper.CardMapper;
import com.example.repository.AccountRepository;
import com.example.repository.CardRepository;
import com.example.service.AccountService;
import com.example.service.CardService;
import com.example.util.enums.AccountType;
import com.example.util.enums.CardStatus;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final AccountMapper accountMapper;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    private static final SecureRandom random = new SecureRandom();


    public Page<CardResponse> getAllCards(Pageable pageable) {
        Page<Card> cardPage = cardRepository.findAll(pageable);
        return cardPage.map(cardMapper::toCardResponse);
    }

    public CardResponse getCardById(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        return cardMapper.toCardResponse(card);
    }

    @Override
    public AccountResponse getAccountByCardId(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        Account account = card.getAccount();
        return accountMapper.toAccountResponse(account);
    }

    @Override
    @Transactional
    public CardDetailResponse issueCard(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        if (account.getCards().stream().anyMatch(c -> c.getStatus() == CardStatus.ACTIVE)) {
            throw new CardAlreadyExistsException(accountId);
        }
        if (account.getType().equals(AccountType.SAVINGS) || account.getType().equals(AccountType.MORTGAGE)) {
            throw new BusinessException("CardResponse issuing is not possible for Savings or Mortgage accounts");
        }
        CustomerResponse customer = accountService.getCustomerByAccountId(accountId);

        Card card = new Card();

        card.setCardNumber(generateCardNumber());
        card.setCardHolder(customer.firstName() + " " + customer.lastName());
        card.setCurrency(account.getCurrency());
        card.setExpiryDate(LocalDate.now().plusYears(4));
        card.setCvvCode(100 + random.nextInt(900));
        card.setPin(1000 + random.nextInt(9000));
        card.setStatus(CardStatus.ISSUED);

        card.setAccount(account);
        account.getCards().add(card);
//        accountRepository.save(account);
        log.info("CardResponse issued for account with ID: {}", accountId);
        return cardMapper.toCardDetailResponse(cardRepository.save(card));
    }

    private String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        cardNumber.append("4");
        for (int i = 1; i < 16; i++) {
            cardNumber.append(random.nextInt(10));
        }
        String result = cardNumber.toString();

        if (result.length() != 16) {
            throw new BusinessException("Generated card number length is not 16: " + result.length());
        }

        while (cardRepository.existsByCardNumber(result)) {
            cardNumber = new StringBuilder("4");
            for (int i = 1; i < 16; i++) {
                cardNumber.append(random.nextInt(10));
            }
            result = cardNumber.toString();
        }

        return result;
    }


    @Override
    @Transactional
    public CardDetailResponse reissueCard(Long id) {
        if (!validateReissueRequest(id)) {
            throw new BusinessException("Reissue is not possible");
        }
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        card.setCardNumber(generateCardNumber());
        card.setExpiryDate(LocalDate.now().plusYears(4));
        card.setCvvCode(100 + random.nextInt(900));
        card.setPin(1000 + random.nextInt(9000));
        card.setStatus(CardStatus.ISSUED);
        log.info("CardResponse reissued for card with ID: {}", id);
        return cardMapper.toCardDetailResponse(cardRepository.save(card));
    }

    private boolean validateReissueRequest(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));

        return card.getAccount().getCards().stream().noneMatch(c -> c.getStatus() == CardStatus.ACTIVE
                || c.getStatus() == CardStatus.ISSUED);
    }

    @Override
    public void activateCard(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        if (card.getStatus() == CardStatus.ISSUED) {
            card.setStatus(CardStatus.ACTIVE);
            cardRepository.save(card);
            log.info("CardResponse activated for card with ID: {}", id);
        }
    }

    @Override
    public void blockCard(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        card.setStatus(CardStatus.BLOCKED);
        card.setBlockedReason("Security reasons");
        cardRepository.save(card);
        log.info("CardResponse blocked for card with ID: {}", id);
    }

    @Override
    public void unblockCard(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        if (card.getStatus() == CardStatus.BLOCKED) {
            card.setStatus(CardStatus.ACTIVE);
            cardRepository.save(card);
            log.info("CardResponse unblocked for card with ID: {}", id);
        }
    }

    @Override
    @Transactional
    public void changePIN(Long id, String oldPin, String newPin) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));

        if (!String.valueOf(card.getPin()).equals(oldPin)) {
            throw new BusinessException("Invalid current PIN");
        }
        card.setPin(Integer.parseInt(newPin));
        cardRepository.save(card);
        log.info("CardResponse PIN changed for card with ID: {}", id);
    }

    @Override
    public CardDetailResponse getDetaijCardById(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        return cardMapper.toCardDetailResponse(card);
    }


}