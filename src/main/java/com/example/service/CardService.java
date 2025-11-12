package com.example.service;

import com.example.dto.response.account.AccountResponse;
import com.example.dto.response.card.CardDetailResponse;
import com.example.dto.response.card.CardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {

    Page<CardResponse> getAllCards(Pageable pageable);

    CardResponse getCardById(Long id);

    AccountResponse getAccountByCardId(Long id);

    CardDetailResponse issueCard(Long accountId);

    CardDetailResponse reissueCard(Long id);

    void activateCard(Long id);

    void blockCard(Long id);

    void unblockCard(Long id);

    void changePIN(Long id, String oldPin, String newPin);

    CardDetailResponse getDetaijCardById(Long id);
}
