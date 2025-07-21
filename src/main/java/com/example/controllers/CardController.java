package com.example.controllers;

import com.example.dtos.CardDto;
import com.example.services.CardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/cards")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping(value = {"/", ""})
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<CardDto>> getAllCards() {
        return ResponseEntity.ok(cardService.allCard());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CardDto> createCard(@Valid @RequestBody CardDto cardDto) {
        CardDto createdCard = cardService.createCard(cardDto);
        return ResponseEntity.created(URI.create("/" + cardDto.getId())).body(createdCard);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CardDto> getCard(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCard(id));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CardDto> updateCard(@PathVariable Long id, @Valid @RequestBody CardDto cardDto) {
        return ResponseEntity.ok(cardService.updateCard(id, cardDto));
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CardDto> patchCard(@PathVariable Long id, @RequestBody CardDto cardDto) {
        return ResponseEntity.ok(cardService.patchCard(id, cardDto));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CardDto> deleteCard(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.deleteCard(id));
    }
}