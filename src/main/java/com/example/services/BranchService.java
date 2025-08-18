package com.example.services;

import com.example.dtos.BranchDto;
import com.example.dtos.overview.BranchOverviewDto;
import com.example.exceptions.AppException;
import com.example.mapers.BranchMapper;
import com.example.models.Branch;
import com.example.repository.BranchRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    public BranchService(BranchRepository branchRepository, BranchMapper branchMapper) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
    }

    public List<BranchOverviewDto> allBranches() {
        return branchMapper.toBranchOverviewDtos(branchRepository.findAll());
    }

    public BranchDto createBranch(@Valid BranchDto branchDto) {
        return branchMapper.toBranchDto(branchRepository.save(branchMapper.toBranch(branchDto)));
    }

    public BranchDto getBranch(Long id) {
        return branchMapper.toBranchDto(branchRepository.findById(id).orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND)));
    }

    public BranchDto updateBranch(Long id, @Valid BranchDto branchDto) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND));
        branchMapper.updateBranch(branch, branchMapper.toBranch(branchDto));
        Branch savedBranch = branchRepository.save(branch);
        return branchMapper.toBranchDto(savedBranch);
    }

    public BranchDto patchBranch(Long id, BranchDto branchDto) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new AppException("Not found", HttpStatus.NOT_FOUND));

        if (branchDto.getName() != null) {
            branch.setName(branchDto.getName());
        }
        if (branchDto.getBicCode() != null) {
            branch.setBicCode(branchDto.getBicCode());
        }
        if (branchDto.getTelephoneNumber() != null) {
            branch.setTelephoneNumber(branchDto.getTelephoneNumber());
        }
        if (branchDto.getEmail() != null) {
            branch.setEmail(branchDto.getEmail());
        }
        return branchMapper.toBranchDto(branchRepository.save(branch));
    }

    public BranchDto deleteBranch(Long id) {
        BranchDto branchDto = getBranch(id);
        branchRepository.deleteById(id);
        return branchDto;
    }
}