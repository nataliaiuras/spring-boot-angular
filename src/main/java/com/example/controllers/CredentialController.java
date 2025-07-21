package com.example.controllers;

import com.example.dtos.CredentialsDto;
import com.example.services.CredentialService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/credentials")
public class CredentialController {
    private final CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @GetMapping(value = {"/", ""})
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<CredentialsDto>> getAllCredentials() {
        return ResponseEntity.ok(credentialService.allCredentials());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CredentialsDto> createCredentials(@Valid @RequestBody CredentialsDto credentialsDto) {
        CredentialsDto createdCredentials = credentialService.createCredentials(credentialsDto);
        return ResponseEntity.created(URI.create("/" + credentialsDto.getId())).body(createdCredentials);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CredentialsDto> getCredentials(@PathVariable Long id) {
        return ResponseEntity.ok(credentialService.getCredentials(id));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CredentialsDto> updateCredentials(@PathVariable Long id, @Valid @RequestBody CredentialsDto credentialsDto) {
        return ResponseEntity.ok(credentialService.updateCredentials(id, credentialsDto));
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CredentialsDto> patchCredentials(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
        return ResponseEntity.ok(credentialService.patchCredentials(id, credentialsDto));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CredentialsDto> deleteCredentials(@PathVariable Long id) {
        return ResponseEntity.ok(credentialService.deleteCredentials(id));
    }
}