package com.example.services;

import com.example.dtos.card.CardDto;
import com.example.dtos.account.AccountOverviewDto;
import com.example.dtos.card.CardOverviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {

    Page<CardOverviewDto> getAllCards(Pageable pageable);

    CardOverviewDto getCardById(Long id);

    CardDto createCard(CardDto cardDto);

    CardDto updateCard(Long id, CardDto cardDto);

    void deleteCard(Long id);

    AccountOverviewDto getAccountByCardId(Long id);
}
