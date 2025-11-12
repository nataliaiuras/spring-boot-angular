package com.example.mapper;

import com.example.entity.Card;
import com.example.dto.response.card.CardDetailResponse;
import com.example.dto.response.card.CardResponse;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CardMapper {

    Set<CardResponse> toCardResponse(Set<Card> cards);

    CardResponse toCardResponse(Card card);

    CardDetailResponse toCardDetailResponse(Card card);
}