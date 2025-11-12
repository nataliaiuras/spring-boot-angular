package com.example.controller;

import com.example.dto.response.general.ApiResponse;
import com.example.dto.response.account.AccountResponse;
import com.example.dto.response.card.CardDetailResponse;
import com.example.dto.response.card.CardResponse;
import com.example.service.CardService;
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
    public ResponseEntity<ApiResponse<Page<CardResponse>>> getAllCards(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<CardResponse> pagedCard = cardService.getAllCards(pageable);
        return ResponseEntity.ok(ApiResponse.success(pagedCard));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<CardResponse>> getCard(@PathVariable Long id) {
        CardResponse response = cardService.getCardById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("{id}/detail")
    public ResponseEntity<ApiResponse<CardDetailResponse>> getDetailCard(@PathVariable Long id) {
        CardDetailResponse response = cardService.getDetaijCardById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("{id}/account")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(@PathVariable Long id) {
        AccountResponse accountResponse = cardService.getAccountByCardId(id);
        return ResponseEntity.ok(ApiResponse.success(accountResponse));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CardDetailResponse>> issueCard(@RequestParam Long accountId) {
        CardDetailResponse issuedCard = cardService.issueCard(accountId);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(issuedCard.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(ApiResponse.success(issuedCard, "CardResponse issued successfully"));
    }

    @PostMapping("{id}/reissue")
    public ResponseEntity<ApiResponse<CardDetailResponse>> reissueCard(@PathVariable Long id) {
        CardDetailResponse reissuedCard = cardService.reissueCard(id);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reissuedCard.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(ApiResponse.success(reissuedCard, "CardResponse reissued successfully"));
    }

    @PutMapping("{id}/activate")
    public ResponseEntity<ApiResponse<String>> activateCard(@PathVariable Long id) {
        cardService.activateCard(id);
        return ResponseEntity.ok(ApiResponse.success("CardResponse activated successfully"));
    }

    @PutMapping("{id}/block")
    public ResponseEntity<ApiResponse<String>> blockCard(@PathVariable Long id) {
        cardService.blockCard(id);
        return ResponseEntity.ok(ApiResponse.success("CardResponse blocked successfully"));
    }

    @PutMapping("{id}/unblock")
    public ResponseEntity<ApiResponse<String>> unblockCard(@PathVariable Long id) {
        cardService.unblockCard(id);
        return ResponseEntity.ok(ApiResponse.success("CardResponse unblocked successfully"));
    }

    @PutMapping("{id}/pin")
    public ResponseEntity<ApiResponse<String>> changePIN(@PathVariable Long id,
                                                         @RequestParam String oldPin,
                                                         @RequestParam String newPin) {
        cardService.changePIN(id, oldPin, newPin);
        return ResponseEntity.ok(ApiResponse.success("PIN changed successfully"));
    }


}