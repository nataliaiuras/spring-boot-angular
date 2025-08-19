package com.example.services;

import com.example.dtos.CardDto;
import com.example.dtos.overview.CardOverviewDto;
import com.example.exceptions.AppException;
import com.example.mapers.CardMapper;
import com.example.models.Card;
import com.example.repository.CardRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    public CardService(CardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    public Set<CardOverviewDto> allCard() {
        return cardMapper.toCardOverviewDtos(new HashSet<>(cardRepository.findAll()));
    }

    public CardDto createCard(@Valid CardDto cardDto) {
        return cardMapper.toCardDto(cardRepository.save(cardMapper.toCard(cardDto)));
    }

    public CardDto getCard(Long id) {
        return cardMapper.toCardDto(cardRepository.findById(id).orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND)));
    }

    public CardDto updateCard(Long id, @Valid CardDto cardDto) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND));
        cardMapper.updateCard(card, cardMapper.toCard(cardDto));
        Card savedCard = cardRepository.save(card);
        return cardMapper.toCardDto(savedCard);
    }

    public CardDto patchCard(Long id, CardDto cardDto) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND));

        if (cardDto.getCardNumber() != null) {
            card.setCardNumber(cardDto.getCardNumber());
        }
        if (cardDto.getCardHolder() != null) {
            card.setCardHolder(cardDto.getCardHolder());
        }
        if (cardDto.getValidThru() != null) {
            card.setValidThru(cardDto.getValidThru());
        }
        if (cardDto.getCvvCode() != 0) {
            card.setCvvCode(cardDto.getCvvCode());
        }
        if (cardDto.getPin() != 0) {
            card.setPin(cardDto.getPin());
        }
        return cardMapper.toCardDto(cardRepository.save(card));
    }

    public CardDto deleteCard(Long id) {
        CardDto cardDto = getCard(id);
        cardRepository.deleteById(id);
        return cardDto;
    }

/*    public CardDto getCard(Long id) {
        return cardRepository.findById(id)
                .map(cardMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));
    }

    public CardDto saveCard(CardDto cardDto) {
        Card card = cardMapper.toEntity(cardDto);
        card = cardRepository.save(card);
        return cardMapper.toDto(card);
    }*/

}