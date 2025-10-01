package com.example.services.impl;

import com.example.exceptions.BusinessException;
import com.example.exceptions.domain.account.AccountNotFoundException;
import com.example.exceptions.domain.card.CardAlreadyExistsException;
import com.example.exceptions.domain.card.CardNotFoundException;
import com.example.models.dtos.account.AccountOverviewDto;
import com.example.models.dtos.card.CardDto;
import com.example.models.dtos.card.CardOverviewDto;
import com.example.models.dtos.customer.CustomerOverviewDto;
import com.example.models.entities.Account;
import com.example.models.entities.Card;
import com.example.repository.AccountRepository;
import com.example.repository.CardRepository;
import com.example.services.AccountService;
import com.example.services.CardService;
import com.example.utils.enums.CardStatus;
import com.example.utils.mapers.AccountMapper;
import com.example.utils.mapers.CardMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final AccountMapper accountMapper;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    private static final SecureRandom random = new SecureRandom();


    public Page<CardOverviewDto> getAllCards(Pageable pageable) {
        Page<Card> cardPage = cardRepository.findAll(pageable);
        return cardPage.map(cardMapper::toCardOverviewDto);
    }

    public CardOverviewDto getCardById(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        return cardMapper.toCardOverviewDto(card);
    }

    @Override
    public AccountOverviewDto getAccountByCardId(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        Account account = card.getAccount();
        return accountMapper.toAccountOverviewDto(account);
    }

    @Override
    @Transactional
    public CardDto issueCard(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        if (accountService.getCardsByAccountId(accountId) != null) {
            throw new CardAlreadyExistsException(accountId);
        }
        CustomerOverviewDto customer = accountService.getCustomerByAccountId(accountId);

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
        return cardMapper.toCardDto(cardRepository.save(card));
    }

    private String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        cardNumber.append("4");
        for (int i = 1; i < 16; i++) {
            cardNumber.append(random.nextInt(10));
        }
        String result = cardNumber.toString();

        if (result.length() != 16) {
            throw new RuntimeException("Generated card number length is not 16: " + result.length());
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
    public CardDto reissueCard(Long id) {
        if (!validateReissueRequest(id)){
            throw new BusinessException("Reissue is not possible");
        }
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        card.setCardNumber(generateCardNumber());
        card.setExpiryDate(LocalDate.now().plusYears(4));
        card.setCvvCode(100 + random.nextInt(900));
        card.setPin(1000 + random.nextInt(9000));
        card.setStatus(CardStatus.ISSUED);
        return cardMapper.toCardDto(cardRepository.save(card));
    }

    private boolean validateReissueRequest(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));

        return card.getAccount().getCards().stream()
                .noneMatch(c -> c.getStatus() == CardStatus.ACTIVE || c.getStatus() == CardStatus.ISSUED);
    }

    @Override
    public void activateCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        if (card.getStatus() == CardStatus.ISSUED) {
            card.setStatus(CardStatus.ACTIVE);
            cardRepository.save(card);
        }
    }

    @Override
    public void blockCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        card.setStatus(CardStatus.BLOCKED);
        card.setBlockedReason("Security reasons");
        cardRepository.save(card);
    }

    @Override
    public void unblockCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        if (card.getStatus() == CardStatus.BLOCKED) {
            card.setStatus(CardStatus.ACTIVE);
            cardRepository.save(card);
        }
    }

    @Override
    @Transactional
    public void changePIN(Long id, String oldPin, String newPin) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));

        if (!String.valueOf(card.getPin()).equals(oldPin)) {
            throw new BusinessException("Invalid current PIN");
        }
        card.setPin(Integer.parseInt(newPin));
        cardRepository.save(card);
    }


}