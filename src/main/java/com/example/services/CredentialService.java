package com.example.services;

import com.example.dtos.CredentialsDto;
import com.example.exceptions.AppException;
import com.example.mapers.CredentialsMapper;
import com.example.models.Credentials;
import com.example.repository.CredentialsRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    private final CredentialsRepository credentialsRepository;
    private final CredentialsMapper credentialsMapper;

    public CredentialService(CredentialsRepository credentialsRepository, CredentialsMapper credentialsMapper) {
        this.credentialsRepository = credentialsRepository;
        this.credentialsMapper = credentialsMapper;
    }

    public List<CredentialsDto> allCredentials() {
        return credentialsMapper.toCredentialsDtos(credentialsRepository.findAll());
    }

    public CredentialsDto createCredentials(@Valid CredentialsDto credentialsDto) {
        return credentialsMapper.toCredentialsDto(credentialsRepository.save(credentialsMapper.toCredentials(credentialsDto)));
    }

    public CredentialsDto getCredentials(Long id) {
        return credentialsMapper.toCredentialsDto(credentialsRepository.findById(id).orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND)));
    }

    public CredentialsDto updateCredentials(Long id, @Valid CredentialsDto credentialsDto) {
        Credentials credentials = credentialsRepository.findById(id)
                .orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND));
        credentialsMapper.updateCredentials(credentials, credentialsMapper.toCredentials(credentialsDto));
        Credentials savedCredentials = credentialsRepository.save(credentials);
        return credentialsMapper.toCredentialsDto(savedCredentials);
    }

    public CredentialsDto patchCredentials(Long id, CredentialsDto credentialsDto) {
        Credentials credentials = credentialsRepository.findById(id)
                .orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND));
        if (credentialsDto.getUsername() != null) {
            credentials.setUsername(credentialsDto.getUsername());
        }
        if (credentialsDto.getPassword() != null) {
            credentials.setPassword(credentialsDto.getPassword());
        }
        return credentialsMapper.toCredentialsDto(credentialsRepository.save(credentials));
    }

    public CredentialsDto deleteCredentials(Long id) {
        CredentialsDto credentialsDto = getCredentials(id);
        credentialsRepository.deleteById(id);
        return credentialsDto;
    }
}