package com.example.services;

import com.example.dtos.BankDto;
import com.example.dtos.overview.BankOverviewDto;
import com.example.dtos.overview.BranchOverviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface BankService {

    Page<BankOverviewDto> getAllBanks(Pageable pageable);

    BankOverviewDto getBankById(Long id);

    BankDto createBank(BankDto bankDto);

    BankOverviewDto updateBank(Long id, BankDto bankDto);

    void deleteBank(Long id);

    Set<BranchOverviewDto> getBranchesByBankId(Long bankId);
}
