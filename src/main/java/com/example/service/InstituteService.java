package com.example.service;

import com.example.dto.request.institute.InstituteCreateRequest;
import com.example.dto.request.institute.InstituteUpdateRequest;
import com.example.dto.response.branch.BranchResponse;
import com.example.dto.response.institute.InstituteDetailResponse;
import com.example.dto.response.institute.InstituteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InstituteService {

    Page<InstituteResponse> getAllInstitutes(Pageable pageable);

    InstituteResponse getInstituteById(Long id);

    InstituteDetailResponse getDetailedInstitute(Long id);

    InstituteDetailResponse createInstitute(InstituteCreateRequest request);

    InstituteDetailResponse updateInstitute(Long id, InstituteUpdateRequest request);

    InstituteDetailResponse patchInstitute(Long id, InstituteUpdateRequest request);

    void deleteInstitute(Long id);

    Page<BranchResponse> getBranchesByInstituteId(Long instituteId, Pageable pageable);

}
