package com.example.services.impl;

import com.example.dtos.bank.BankDto;
import com.example.dtos.bank.BankOverviewDto;
import com.example.dtos.bank.BankUpdateDto;
import com.example.dtos.branch.BranchOverviewDto;
import com.example.dtos.bank.BankRequestDto;
import com.example.entities.Bank;
import com.example.entities.Branch;
import com.example.exceptions.domain.bank.BankAlreadyExistsException;
import com.example.exceptions.domain.bank.BankNotFoundException;
import com.example.mapers.BankMapper;
import com.example.mapers.BranchMapper;
import com.example.repository.BankRepository;
import com.example.services.BankService;
import com.example.utils.AddressType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

    public BankDto createBank(BankRequestDto dto) {
        if (bankRepository.findByName(dto.getName()).isPresent()
                || bankRepository.findByWebsite(dto.getWebsite()).isPresent()) {
            throw new BankAlreadyExistsException(dto.getName());
        }
        Bank bank = bankMapper.toBank(dto);
        bank.setBranches(new HashSet<>());
        bank.getAddress().setAddressType(AddressType.BRANCH);
        return bankMapper.toBankDto(bankRepository.save(bank));
    }

    public BankDto updateBank(Long id, BankUpdateDto dto) {
        Bank bank = bankRepository.findById(id).orElseThrow(() -> new BankNotFoundException(id));
        bankMapper.updateBank(bank, bankMapper.toBank(dto));
        bank.setLastModifiedDate(Instant.now());
        bank.setVersion(bank.getVersion() + 1);
        return bankMapper.toBankDto(bankRepository.save(bank));
    }

    public BankDto patchBank(Long id, BankUpdateDto dto) {
        Bank bank = bankRepository.findById(id).orElseThrow(() -> new BankNotFoundException(id));
        if (dto.getBankCode() != null) {
            bank.setBankCode(dto.getBankCode());
        }
        if (dto.getName() != null) {
            bank.setName(dto.getName());
        }
        if (dto.getWebsite() != null){
            bank.setWebsite(dto.getWebsite());
        }
        bankMapper.updateBank(bank, bankMapper.toBank(dto));
        bank.setLastModifiedDate(Instant.now());
        bank.setVersion(bank.getVersion() + 1);
        return bankMapper.toBankDto(bankRepository.save(bank));
    }

    public void deleteBank(Long id) {
        bankRepository.delete(bankRepository.findById(id).orElseThrow(() -> new BankNotFoundException(id)));
    }

    public Set<BranchOverviewDto> getBranchesByBankId(Long bankId) {
        Bank bank = bankRepository.findById(bankId).orElseThrow(() -> new BankNotFoundException(bankId));
        Set<Branch> branches = bank.getBranches();
        return branchMapper.toBranchOverviewDtos(branches);
    }

}
