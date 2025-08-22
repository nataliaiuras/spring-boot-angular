package com.example.services;

import com.example.dtos.BranchDto;
import com.example.dtos.overview.BranchOverviewDto;
import com.example.entities.Bank;
import com.example.entities.Branch;
import com.example.exceptions.BusinessException;
import com.example.exceptions.domain.bank.BankNotFoundException;
import com.example.exceptions.domain.branch.BranchAlreadyExistsException;
import com.example.exceptions.domain.branch.BranchNotFoundException;
import com.example.mapers.BranchMapper;
import com.example.repository.BankRepository;
import com.example.repository.BranchRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final BankRepository bankRepository;
    private final BranchMapper branchMapper;

    public Page<BranchOverviewDto> getAllBranches(Pageable pageable) {
        Page<Branch> branchPage = branchRepository.findAll(pageable);
        return branchPage.map(branchMapper::toBranchOverviewDto);
    }

    public BranchDto getBranchById(Long id) {
        return branchMapper.toBranchDto(branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException(id)));
    }

    public BranchDto createBranch(BranchDto branchDto) {
        if (branchRepository.findByName(branchDto.getName()).isPresent()) {
            throw new BranchAlreadyExistsException(branchDto.getId());
        }
        Branch branch = branchMapper.toBranch(branchDto);
        branch.setId(null);
        if (branch.getBank() == null || branch.getBank().getId() == null) {
            throw new BusinessException("Bank ID is required when creating a branch", HttpStatus.BAD_REQUEST);
        }
        Bank bank = bankRepository.findById(branchDto.getBank().id())
                .orElseThrow(() -> new BankNotFoundException(branchDto.getBank().id()));
        branch.setAddress(branch.getAddress());
        branch.setBank(bank);
        Branch savedBranch = branchRepository.save(branch);
        return branchMapper.toBranchDto(savedBranch);
    }

    public BranchDto updateBranch(Long id, BranchDto branchDto) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException(id));
        branchMapper.updateBranch(branch, branchMapper.toBranch(branchDto));
        return branchMapper.toBranchDto(branchRepository.save(branch));
    }

    public void deleteBranch(Long id) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException(id));
        Bank bank = branch.getBank();
        if (bank != null) {
            bank.getBranches().remove(branch);
            bankRepository.save(bank);
        }
        branchRepository.delete(branch);
    }


    public Set<BranchDto> getBranchesByBankId(Long bankId) {
        return null;
    }
}