package com.example.services;

import com.example.dtos.BankDto;
import com.example.dtos.overview.BankOverviewDto;
import com.example.exceptions.AppException;
import com.example.mapers.BankMapper;
import com.example.models.Bank;
import com.example.models.Branch;
import com.example.repository.BankRepository;
import com.example.repository.BranchRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class BankService {

    private final BankRepository bankRepository;
    private final BranchRepository branchRepository;
    private final BankMapper bankMapper;

    public BankService(BankRepository bankRepository, BranchRepository branchRepository, BankMapper bankMapper) {
        this.bankRepository = bankRepository;
        this.branchRepository = branchRepository;
        this.bankMapper = bankMapper;
    }

    public Set<BankOverviewDto> allBanks() {
        return bankMapper.toBankOverviewDtos(new HashSet<>(bankRepository.findAll()));
    }

    public BankDto getBank(Long id) {
        return bankMapper.toBankDto(bankRepository.findById(id).orElseThrow(() -> new AppException("Bank not found", HttpStatus.NOT_FOUND)));
    }

    public BankDto createBank(@Valid BankDto bankDto) {
        if (bankRepository.findByName(bankDto.getName()).isPresent() || bankRepository.findByWebsite(bankDto.getWebsite()).isPresent()) {
            throw new AppException("Bank already exists", HttpStatus.CONFLICT);
        }
        Bank bank = bankMapper.toBank(bankDto);
        bank.setId(null);
        bank.setBranches(new HashSet<>());
        Bank savedBank = bankRepository.save(bank);
        return bankMapper.toBankDto(savedBank);
    }

    public BankDto updateBank(Long id, @Valid BankDto bankDto) {
        Bank existingBank = bankRepository.findById(id).orElseThrow(() -> new AppException("Bank not found", HttpStatus.NOT_FOUND));
        bankMapper.updateBank(existingBank, bankMapper.toBank(bankDto));
        return bankMapper.toBankDto(bankRepository.save(existingBank));
    }

    public BankDto patchBank(Long id, BankDto bankDto) {
        Bank bank = bankRepository.findById(id).orElseThrow(() -> new AppException("Bank not found", HttpStatus.NOT_FOUND));
        if (bankDto.getName() != null) {
            bank.setName(bankDto.getName());
        }
        if (bankDto.getWebsite() != null) {
            bank.setWebsite(bankDto.getWebsite());
        }
        return bankMapper.toBankDto(bankRepository.save(bank));
    }

    public void deleteBank(Long id) {
        bankRepository.deleteById(id);
    }

    public BankDto addBranchToBank(Long bankId, Long branchId) {
        Bank bank = bankRepository.findById(bankId).orElseThrow(() -> new AppException("Bank not found", HttpStatus.NOT_FOUND));
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new AppException("Branch not found", HttpStatus.NOT_FOUND));
        if (bank.getBranches().stream().anyMatch(b -> b.getId().equals(branchId))) {
            throw new AppException("Branch already belongs to this bank", HttpStatus.CONFLICT);
        }
        branch.setBank(bank);
        bank.getBranches().add(branch);
        branchRepository.save(branch);
        return bankMapper.toBankDto(bankRepository.save(bank));

    }

    public BankDto removeBranchFromBank(Long bankId, Long branchId) {
        Bank bank = bankRepository.findById(bankId).orElseThrow(() -> new AppException("Bank not found", HttpStatus.NOT_FOUND));
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new AppException("Branch not found", HttpStatus.NOT_FOUND));
        if (!bank.getBranches().contains(branch)) {
            throw new AppException("Branch does not belong to this bank", HttpStatus.CONFLICT);
        }
        bank.getBranches().remove(branch);
        branch.setBank(null);
        branchRepository.save(branch);
        return bankMapper.toBankDto(bankRepository.save(bank));

    }


}
