package com.example.services;

import com.example.dtos.bank.BankDto;
import com.example.dtos.bank.BankOverviewDto;
import com.example.dtos.bank.BankUpdateDto;
import com.example.dtos.branch.BranchOverviewDto;
import com.example.dtos.bank.BankRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface BankService {

    Page<BankOverviewDto> getAllBanks(Pageable pageable);

    BankOverviewDto getBankById(Long id);

    BankDto createBank(BankRequestDto dto);

    BankDto updateBank(Long id, BankUpdateDto dto);

    BankDto patchBank(Long id, BankUpdateDto dto);

    void deleteBank(Long id);

    Set<BranchOverviewDto> getBranchesByBankId(Long bankId);
}
