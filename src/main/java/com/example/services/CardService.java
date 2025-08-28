package com.example.services;

import com.example.dtos.CardDto;
import com.example.dtos.overview.AccountOverviewDto;
import com.example.dtos.overview.CardOverviewDto;
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
