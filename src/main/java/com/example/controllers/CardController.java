package com.example.controllers;

import com.example.dtos.CardDto;
import com.example.dtos.overview.CardOverviewDto;
import com.example.dtos.response.ApiResponse;
import com.example.services.impl.CardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/cards")
@AllArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<ApiResponse<CardDto>> createCard(@Valid @RequestBody CardDto cardDto) {
        CardDto createdCard = cardService.createCard(cardDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdCard.getId()).toUri();
        return ResponseEntity.created(location).body(ApiResponse.success(createdCard, "Card created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CardOverviewDto>>> getAllCards(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                                          @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
                                                                          @RequestParam(defaultValue = "id") String sortBy,
                                                                          @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<CardOverviewDto> pagedCard = cardService.getAllCards(pageable);
        return ResponseEntity.ok(ApiResponse.success(pagedCard));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<CardDto>> getCard(@PathVariable Long id) {
        CardDto cardDto = cardService.getCardById(id);
        return ResponseEntity.ok(ApiResponse.success(cardDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<CardDto>> updateCard(@PathVariable Long id, @Valid @RequestBody CardDto cardDto) {
        CardDto updateCard = cardService.updateCard(id, cardDto);
        return ResponseEntity.ok(ApiResponse.success(updateCard, "Card updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.ok(ApiResponse.success("Card  deleted successfully"));
    }


}