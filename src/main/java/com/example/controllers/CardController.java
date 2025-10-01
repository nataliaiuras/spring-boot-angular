package com.example.controllers;

import com.example.exceptions.response.ApiResponse;
import com.example.models.dtos.account.AccountOverviewDto;
import com.example.models.dtos.card.CardDto;
import com.example.models.dtos.card.CardOverviewDto;
import com.example.services.CardService;
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

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CardOverviewDto>>> getAllCards(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<CardOverviewDto> pagedCard = cardService.getAllCards(pageable);
        return ResponseEntity.ok(ApiResponse.success(pagedCard));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<CardOverviewDto>> getCard(@PathVariable Long id) {
        CardOverviewDto cardDto = cardService.getCardById(id);
        return ResponseEntity.ok(ApiResponse.success(cardDto));
    }

    @GetMapping("{id}/account")
    public ResponseEntity<ApiResponse<AccountOverviewDto>> getAccount(@PathVariable Long id) {
        AccountOverviewDto accountOverviewDto = cardService.getAccountByCardId(id);
        return ResponseEntity.ok(ApiResponse.success(accountOverviewDto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CardDto>> issueCard(@RequestParam Long accountId) {
        CardDto issuedCard = cardService.issueCard(accountId);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(issuedCard.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(ApiResponse.success(issuedCard, "Card issued successfully"));
    }

    @PostMapping("{id}/reissue")
    public ResponseEntity<ApiResponse<CardDto>> reissueCard(@PathVariable Long id) {
        CardDto reissuedCard = cardService.reissueCard(id);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reissuedCard.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(ApiResponse.success(reissuedCard, "Card reissued successfully"));
    }

    @PutMapping("{id}/activate")
    public ResponseEntity<ApiResponse<String>> activateCard(@PathVariable Long id) {
        cardService.activateCard(id);
        return ResponseEntity.ok(ApiResponse.success("Card activated successfully"));
    }

    @PutMapping("{id}/block")
    public ResponseEntity<ApiResponse<String>> blockCard(@PathVariable Long id) {
        cardService.blockCard(id);
        return ResponseEntity.ok(ApiResponse.success("Card blocked successfully"));
    }

    @PutMapping("{id}/unblock")
    public ResponseEntity<ApiResponse<String>> unblockCard(@PathVariable Long id) {
        cardService.unblockCard(id);
        return ResponseEntity.ok(ApiResponse.success("Card unblocked successfully"));
    }

    @PutMapping("{id}/pin")
    public ResponseEntity<ApiResponse<String>> changePIN(@PathVariable Long id,
                                                         @RequestParam String oldPin,
                                                         @RequestParam String newPin) {
        cardService.changePIN(id, oldPin, newPin);
        return ResponseEntity.ok(ApiResponse.success("PIN changed successfully"));
    }


}