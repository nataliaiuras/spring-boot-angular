package com.example.services.impl;

import com.example.exceptions.domain.institute.InstituteAlreadyExistsException;
import com.example.exceptions.domain.institute.InstituteNotFoundException;
import com.example.models.dtos.institute.InstituteCreationDto;
import com.example.models.dtos.institute.InstituteDto;
import com.example.models.dtos.institute.InstituteOverviewDto;
import com.example.models.dtos.institute.InstituteUpdateDto;
import com.example.models.dtos.branch.BranchOverviewDto;
import com.example.models.entities.Institute;
import com.example.models.entities.Branch;
import com.example.repository.InstituteRepository;
import com.example.repository.BranchRepository;
import com.example.services.InstituteService;
import com.example.utils.mapers.AddressMapper;
import com.example.utils.mapers.InstituteMapper;
import com.example.utils.mapers.BranchMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;

@Service
@Transactional
@AllArgsConstructor
public class InstituteServiceImpl implements InstituteService {

    private final InstituteRepository instituteRepository;
    private final InstituteMapper instituteMapper;
    private final BranchMapper branchMapper;
    private final BranchRepository branchRepository;
    private final AddressMapper addressMapper;

    public Page<InstituteOverviewDto> getAllInstitutes(Pageable pageable) {
        Page<Institute> institutePage = instituteRepository.findAll(pageable);
        return institutePage.map(instituteMapper::toInstituteOverviewDto);
    }

    public InstituteOverviewDto getInstituteById(Long id) {
        Institute institute = instituteRepository.findById(id).orElseThrow(() -> new InstituteNotFoundException(id));
        return instituteMapper.toInstituteOverviewDto(institute);
    }

    @Override
    public InstituteDto getDetailedInstitute(Long id) {
        Institute institute = instituteRepository.findById(id).orElseThrow(() -> new InstituteNotFoundException(id));
        return instituteMapper.toInstituteDto(institute);
    }

    public InstituteDto createInstitute(InstituteCreationDto dto) {
        if (instituteRepository.findByName(dto.getName()).isPresent()
                || instituteRepository.findByWebsite(dto.getWebsite()).isPresent()) {
            throw new InstituteAlreadyExistsException(dto.getName());
        }
        Institute institute = instituteMapper.toInstitute(dto);
        institute.setBranches(new HashSet<>());
        return instituteMapper.toInstituteDto(instituteRepository.save(institute));
    }

    public InstituteDto updateInstitute(Long id, InstituteUpdateDto dto) {
        Institute institute = instituteRepository.findById(id).orElseThrow(() -> new InstituteNotFoundException(id));
        institute.setBankCode(dto.getBankCode());
        institute.setName(dto.getName());
        institute.setWebsite(dto.getWebsite());

        institute.setLastModifiedDate(Instant.now());
        institute.setVersion(institute.getVersion() + 1);

        return instituteMapper.toInstituteDto(instituteRepository.save(institute));
    }

    public InstituteDto patchInstitute(Long id, InstituteUpdateDto dto) {
        Institute institute = instituteRepository.findById(id).orElseThrow(() -> new InstituteNotFoundException(id));
        if (dto.getBankCode() != null) {
            institute.setBankCode(dto.getBankCode());
        }
        if (dto.getName() != null) {
            institute.setName(dto.getName());
        }
        if (dto.getWebsite() != null) {
            institute.setWebsite(dto.getWebsite());
        }

        institute.setLastModifiedDate(Instant.now());
        institute.setVersion(institute.getVersion() + 1);

        return instituteMapper.toInstituteDto(instituteRepository.save(institute));
    }

    public void deleteInstitute(Long id) {
        instituteRepository.delete(instituteRepository.findById(id).orElseThrow(() -> new InstituteNotFoundException(id)));
    }

    public Page<BranchOverviewDto> getBranchesByInstituteId(Long instituteId, Pageable pageable) {
        if (!instituteRepository.existsById(instituteId)) {
            throw new InstituteNotFoundException(instituteId);
        }
        Page<Branch> branchPage = branchRepository.findByInstituteId(instituteId, pageable);
        return branchPage.map(branchMapper::toBranchOverviewDto);
    }


}
