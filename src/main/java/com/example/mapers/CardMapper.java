package com.example.mapers;

import com.example.dtos.CardDto;
import com.example.dtos.overview.CardOverviewDto;
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

    CardOverviewDto toCardOverviewDto(Card card);

    List<CardOverviewDto> toCardOverviewDtos(List<Card> cards);

/*import com.example.dtos.CardDto;
import com.example.models.Card;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public CardDto toDto(Card card) {
        if (card == null) {
            return null;
        }

        return CardDto.builder()
                .id(card.getId())
                .cardNumber(card.getCardNumber())
                .cardHolder(card.getCardHolder())
                .validThru(card.getValidThru())
                .cvvCode(card.getCvvCode()) // Will be handled by JsonSerialize
                .pin(card.getPin()) // Will be handled by JsonSerialize
                .account(card.getAccount())
                .createdDate(card.getCreatedDate() != null ? card.getCreatedDate().toString() : null)
                .lastModifiedDate(card.getLastModifiedDate() != null ? card.getLastModifiedDate().toString() : null)
                .version(card.getVersion() != null ? card.getVersion().toString() : null)
                .build();
    }

    public Card toEntity(CardDto dto) {
        if (dto == null) {
            return null;
        }

        Card card = new Card();
        card.setId(dto.getId());
        card.setCardNumber(dto.getCardNumber());
        card.setCardHolder(dto.getCardHolder());
        card.setValidThru(dto.getValidThru());
        card.setCvvCode(dto.getCvvCode());
        card.setPin(dto.getPin());
        card.setAccount(dto.getAccount());
        return card;
    }*/


}