package com.example.mapers;

import com.example.dtos.CardDto;
import com.example.dtos.overview.CardOverviewDto;
import com.example.models.Card;
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

    Set<CardDto> toCardDtos(Set<Card> cards);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void updateCard(@MappingTarget Card target, Card source);

    CardOverviewDto toCardOverviewDto(Card card);

    Set<CardOverviewDto> toCardOverviewDtos(Set<Card> cards);


}