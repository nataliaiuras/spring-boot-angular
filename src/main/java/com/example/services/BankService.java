package com.example.services;

import com.example.dtos.BankDto;
import com.example.dtos.overview.BankOverviewDto;
import com.example.exceptions.AppException;
import com.example.mapers.BankMapper;
import com.example.models.Bank;
import com.example.repository.BankRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class BankService {

    private final BankRepository bankRepository;
    private final BankMapper bankMapper;

    public BankService(BankRepository bankRepository, BankMapper bankMapper) {
        this.bankRepository = bankRepository;
        this.bankMapper = bankMapper;
    }

    public List<BankOverviewDto> allBank() {
        return bankMapper.toBankOverviewDtos(bankRepository.findAll());
    }

    @Transactional
    public BankDto createBank(@Valid BankDto bankDto) {

        if (bankRepository.findByName(bankDto.getName()).isPresent()
                || bankRepository.findByWebsite(bankDto.getWebsite()).isPresent()) {
            throw new AppException("Bank already exists", HttpStatus.CONFLICT);
        }

        Bank bank = bankMapper.toBank(bankDto);
        bank.setId(null);
        bank.setBranches(new HashSet<>());

        Bank savedBank = bankRepository.save(bank);
        return bankMapper.toBankDto(savedBank);
    }

    public BankDto getBank(Long id) {
        return bankMapper.toBankDto(bankRepository.findById(id).orElseThrow(() -> new AppException("Bank not found", HttpStatus.NOT_FOUND)));
    }

    @Transactional
    public BankDto updateBank(Long id, @Valid BankDto bankDto) {
        Bank existingBank = bankRepository.findById(id)
                .orElseThrow(() -> new AppException("Bank not found", HttpStatus.NOT_FOUND));

        bankMapper.updateBank(existingBank, bankMapper.toBank(bankDto));
        return bankMapper.toBankDto(bankRepository.save(existingBank));
    }

    public BankDto patchBank(Long id, BankDto bankDto) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new AppException("Bank not found", HttpStatus.NOT_FOUND));

        if (bankDto.getName() != null) {
            bank.setName(bankDto.getName());
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

    public List<BankOverviewDto> getAllBanksOverview() {
        return bankMapper.toBankOverviewDtos(bankRepository.findAll());
    }

    public BankDto getBankDetail(Long id) {
        return bankMapper.toBankDto(bankRepository.findById(id).orElseThrow(() -> new AppException("Bank not found", HttpStatus.NOT_FOUND)));
    }


}
