package com.example.services.impl;

import com.example.dtos.BankDto;
import com.example.dtos.overview.BankOverviewDto;
import com.example.dtos.overview.BranchOverviewDto;
import com.example.entities.Bank;
import com.example.entities.Branch;
import com.example.exceptions.domain.bank.BankAlreadyExistsException;
import com.example.exceptions.domain.bank.BankNotFoundException;
import com.example.mapers.BankMapper;
import com.example.mapers.BranchMapper;
import com.example.repository.BankRepository;
import com.example.services.BankService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;
    private final BankMapper bankMapper;
    private final BranchMapper branchMapper;

    public Page<BankOverviewDto> getAllBanks(Pageable pageable) {
        Page<Bank> bankPage = bankRepository.findAll(pageable);
        return bankPage.map(bankMapper::toBankOverviewDto);
    }

    public BankOverviewDto getBankById(Long id) {
        Bank bank = bankRepository.findById(id).orElseThrow(() -> new BankNotFoundException(id));
        return bankMapper.toBankOverviewDto(bank);
    }

    public BankDto createBank(BankDto bankDto) {
        if (bankRepository.findByName(bankDto.getName()).isPresent()
                || bankRepository.findByWebsite(bankDto.getWebsite()).isPresent()) {
            throw new BankAlreadyExistsException(bankDto.getName());
        }
        Bank bank = bankMapper.toBank(bankDto);
        bank.setBranches(new HashSet<>());
        return bankMapper.toBankDto(bankRepository.save(bank));
    }

    public BankOverviewDto updateBank(Long id, BankDto bankDto) {
        Bank bank = bankRepository.findById(id).orElseThrow(() -> new BankNotFoundException(id));
        bankMapper.updateBank(bank, bankMapper.toBank(bankDto));
        return bankMapper.toBankOverviewDto(bankRepository.save(bank));
    }

    public void deleteBank(Long id) {
        bankRepository.delete(bankRepository.findById(id).orElseThrow(() -> new BankNotFoundException(id)));
    }

    public Set<BranchOverviewDto> getBranchesByBankId(Long bankId) {
        Bank bank = bankRepository.findById(bankId).orElseThrow(() -> new BankNotFoundException(bankId));
        Set<Branch> branches = bank.getBranches();
        return branchMapper.toBranchOverViewDtos(branches);
    }

}
