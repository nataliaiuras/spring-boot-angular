package com.example.services;

import com.example.models.dtos.account.AccountOverviewDto;
import com.example.models.dtos.card.CardDto;
import com.example.models.dtos.card.CardOverviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {

    Page<CardOverviewDto> getAllCards(Pageable pageable);

    CardOverviewDto getCardById(Long id);

    AccountOverviewDto getAccountByCardId(Long id);

    CardDto issueCard(Long accountId);

    CardDto reissueCard(Long id);

    void activateCard(Long id);

    void blockCard(Long id);

    void unblockCard(Long id);

    void changePIN(Long id, String oldPin, String newPin);
}
