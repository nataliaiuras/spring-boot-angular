package com.example.utils.mapers;

import com.example.models.dtos.card.CardDto;
import com.example.models.dtos.card.CardOverviewDto;
import com.example.models.entities.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Card toCard(CardDto cardDto);

    CardDto toCardDto(Card card);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void updateCard(@MappingTarget Card target, Card source);


    CardOverviewDto toCardOverviewDto(Card card);

    Set<CardOverviewDto> toOverviewDtos(Set<Card> cards);
}