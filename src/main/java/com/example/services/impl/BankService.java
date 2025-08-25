package com.example.services.impl;

import com.example.dtos.BankDto;
import com.example.dtos.overview.BankOverviewDto;
import com.example.entities.Bank;
import com.example.exceptions.domain.bank.BankAlreadyExistsException;
import com.example.exceptions.domain.bank.BankNotFoundException;
import com.example.mapers.BankMapper;
import com.example.repository.BankRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@Transactional
@AllArgsConstructor
public class BankService {

    private final BankRepository bankRepository;
    private final BankMapper bankMapper;

    public Page<BankOverviewDto> getAllBanks(Pageable pageable) {
        Page<Bank> bankPage = bankRepository.findAll(pageable);
        return bankPage.map(bankMapper::toBankOverviewDto);
    }

    public BankDto getBankById(Long id) {
        return bankMapper.toBankDto(bankRepository.findById(id).orElseThrow(() -> new BankNotFoundException(id)));
    }

    public BankDto createBank(BankDto bankDto) {
        if (bankRepository.findByName(bankDto.getName()).isPresent() || bankRepository.findByWebsite(bankDto.getWebsite()).isPresent()) {
            throw new BankAlreadyExistsException(bankDto.getId());
        }
        Bank bank = bankMapper.toBank(bankDto);
        bank.setBranches(new HashSet<>());
        return bankMapper.toBankDto(bankRepository.save(bank));
    }

    public BankDto updateBank(Long id, BankDto bankDto) {
        Bank bank = bankRepository.findById(id).orElseThrow(() -> new BankNotFoundException(id));
        bankMapper.updateBank(bank, bankMapper.toBank(bankDto));
        return bankMapper.toBankDto(bankRepository.save(bank));
    }

    public void deleteBank(Long id) {
        bankRepository.delete(bankRepository.findById(id).orElseThrow(() -> new BankNotFoundException(id)));
    }

}
