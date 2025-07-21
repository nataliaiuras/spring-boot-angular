package com.example.services;

import com.example.dtos.BankDto;
import com.example.exceptions.AppException;
import com.example.mapers.BankMapper;
import com.example.models.Bank;
import com.example.repository.BankRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {
    private final BankRepository bankRepository;
    private final BankMapper bankMapper;

    public BankService(BankRepository bankRepository, BankMapper bankMapper) {
        this.bankRepository = bankRepository;
        this.bankMapper = bankMapper;
    }

    public List<BankDto> allBank() {
        return bankMapper.toBankDtos(bankRepository.findAll());
    }

    public BankDto createBank(@Valid BankDto bankDto) {
        return bankMapper.toBankDto(bankRepository.save(bankMapper.toBank(bankDto)));
    }

    public BankDto getBank(Long id) {
        return bankMapper.toBankDto(bankRepository.findById(id).orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND)));
    }

    public BankDto updateBank(Long id, @Valid BankDto bankDto) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND));
        bankMapper.updateBank(bank, bankMapper.toBank(bankDto));
        Bank savedBank = bankRepository.save(bank);
        return bankMapper.toBankDto(savedBank);
    }

    public BankDto patchBank(Long id, BankDto bankDto) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND));

        if (bankDto.getName() != null) {
            bank.setName(bankDto.getName());
        }
        if (bankDto.getTelephoneNumber() != null) {
            bank.setTelephoneNumber(bankDto.getTelephoneNumber());
        }
        if (bankDto.getEmail() != null) {
            bank.setEmail(bankDto.getEmail());
        }
        if (bankDto.getWebsite() != null) {
            bank.setWebsite(bankDto.getWebsite());
        }

        return bankMapper.toBankDto(bankRepository.save(bank));
    }

    public BankDto deleteBank(Long id) {
        BankDto bankDto = getBank(id);
        bankRepository.deleteById(id);
        return bankDto;
    }
}
