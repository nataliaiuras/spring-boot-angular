package com.example.services.impl;

import com.example.dtos.card.CardDto;
import com.example.dtos.account.AccountOverviewDto;
import com.example.dtos.card.CardOverviewDto;
import com.example.entities.Account;
import com.example.entities.Card;
import com.example.exceptions.domain.card.CardNotFoundException;
import com.example.mapers.AccountMapper;
import com.example.mapers.CardMapper;
import com.example.repository.CardRepository;
import com.example.services.CardService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final AccountMapper accountMapper;

    public Page<CardOverviewDto> getAllCards(Pageable pageable) {
        Page<Card> cardPage = cardRepository.findAll(pageable);
        return cardPage.map(cardMapper::toCardOverviewDto);
    }

    public CardDto createCard(CardDto cardDto) {
        return cardMapper.toCardDto(cardRepository.save(cardMapper.toCard(cardDto)));
    }

    public CardOverviewDto getCardById(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        return cardMapper.toCardOverviewDto(card);
    }

    public CardDto updateCard(Long id, CardDto cardDto) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        cardMapper.updateCard(card, cardMapper.toCard(cardDto));
        return cardMapper.toCardDto(cardRepository.save(card));
    }

    public void deleteCard(Long id) {
        cardRepository.delete(cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id)));
    }

    @Override
    public AccountOverviewDto getAccountByCardId(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        Account account = card.getAccount();
        return accountMapper.toAccountOverviewDto(account);
    }

}