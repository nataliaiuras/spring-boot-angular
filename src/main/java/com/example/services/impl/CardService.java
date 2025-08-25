package com.example.services.impl;

import com.example.dtos.CardDto;
import com.example.dtos.overview.CardOverviewDto;
import com.example.entities.Card;
import com.example.exceptions.domain.card.CardNotFoundException;
import com.example.mapers.CardMapper;
import com.example.repository.CardRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    public Page<CardOverviewDto> getAllCards(Pageable pageable) {
        Page<Card> cardPage = cardRepository.findAll(pageable);
        return cardPage.map(cardMapper::toCardOverviewDto);
    }

    public CardDto createCard(CardDto cardDto) {
        return cardMapper.toCardDto(cardRepository.save(cardMapper.toCard(cardDto)));
    }

    public CardDto getCardById(Long id) {
        return cardMapper.toCardDto(cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id)));
    }

    public CardDto updateCard(Long id, CardDto cardDto) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        cardMapper.updateCard(card, cardMapper.toCard(cardDto));
        return cardMapper.toCardDto(cardRepository.save(card));
    }

    public void deleteCard(Long id) {
        cardRepository.delete(cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id)));
    }

}