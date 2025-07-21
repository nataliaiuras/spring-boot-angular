package com.example.mapers;

import com.example.dtos.CardDto;
import com.example.models.Card;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardMapper {

    Card toCard(CardDto cardDto);

    CardDto toCardDto(Card card);

    List<CardDto> toCardDtos(List<Card> cards);

    void updateCard(@MappingTarget Card target, Card source);
}