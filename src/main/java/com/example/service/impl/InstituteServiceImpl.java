package com.example.service.impl;

import com.example.dto.request.institute.InstituteCreateRequest;
import com.example.dto.request.institute.InstituteUpdateRequest;
import com.example.dto.response.branch.BranchResponse;
import com.example.dto.response.institute.InstituteDetailResponse;
import com.example.dto.response.institute.InstituteResponse;
import com.example.entity.Branch;
import com.example.entity.Institute;
import com.example.exception.domain.institute.InstituteAlreadyExistsException;
import com.example.exception.domain.institute.InstituteNotFoundException;
import com.example.mapper.AddressMapper;
import com.example.mapper.BranchMapper;
import com.example.mapper.InstituteMapper;
import com.example.repository.BranchRepository;
import com.example.repository.InstituteRepository;
import com.example.service.InstituteService;
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

    public Page<InstituteResponse> getAllInstitutes(Pageable pageable) {
        Page<Institute> institutePage = instituteRepository.findAll(pageable);
        return institutePage.map(instituteMapper::toInstituteResponse);
    }

    public InstituteResponse getInstituteById(Long id) {
        Institute institute = instituteRepository.findById(id).orElseThrow(() -> new InstituteNotFoundException(id));
        return instituteMapper.toInstituteResponse(institute);
    }

    @Override
    public InstituteDetailResponse getDetailedInstitute(Long id) {
        Institute institute = instituteRepository.findById(id).orElseThrow(() -> new InstituteNotFoundException(id));
        return instituteMapper.toInstituteDetailResponse(institute);
    }

    public InstituteDetailResponse createInstitute(InstituteCreateRequest request) {
        if (instituteRepository.findByName(request.getName()).isPresent()
                || instituteRepository.findByWebsite(request.getWebsite()).isPresent()) {
            throw new InstituteAlreadyExistsException(request.getName());
        }
        Institute institute = instituteMapper.toInstitute(request);
        institute.setBranches(new HashSet<>());
        return instituteMapper.toInstituteDetailResponse(instituteRepository.save(institute));
    }

    public InstituteDetailResponse updateInstitute(Long id, InstituteUpdateRequest request) {
        Institute institute = instituteRepository.findById(id).orElseThrow(() -> new InstituteNotFoundException(id));
        institute.setBankCode(request.getBankCode());
        institute.setName(request.getName());
        institute.setWebsite(request.getWebsite());
        return instituteMapper.toInstituteDetailResponse(instituteRepository.save(institute));
    }

    public InstituteDetailResponse patchInstitute(Long id, InstituteUpdateRequest request) {
        Institute institute = instituteRepository.findById(id).orElseThrow(() -> new InstituteNotFoundException(id));
        if (request.getBankCode() != null) {
            institute.setBankCode(request.getBankCode());
        }
        if (request.getName() != null) {
            institute.setName(request.getName());
        }
        if (request.getWebsite() != null) {
            institute.setWebsite(request.getWebsite());
        }
        return instituteMapper.toInstituteDetailResponse(instituteRepository.save(institute));
    }

    public void deleteInstitute(Long id) {
        instituteRepository.delete(instituteRepository.findById(id).orElseThrow(() -> new InstituteNotFoundException(id)));
    }

    public Page<BranchResponse> getBranchesByInstituteId(Long instituteId, Pageable pageable) {
        if (!instituteRepository.existsById(instituteId)) {
            throw new InstituteNotFoundException(instituteId);
        }
        Page<Branch> branchPage = branchRepository.findByInstituteId(instituteId, pageable);
        return branchPage.map(branchMapper::toBranchResponse);
    }


}
