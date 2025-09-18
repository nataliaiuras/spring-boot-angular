package com.example.services;

import com.example.models.dtos.institute.InstituteCreationDto;
import com.example.models.dtos.institute.InstituteDto;
import com.example.models.dtos.institute.InstituteOverviewDto;
import com.example.models.dtos.institute.InstituteUpdateDto;
import com.example.models.dtos.branch.BranchOverviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InstituteService {

    Page<InstituteOverviewDto> getAllInstitutes(Pageable pageable);

    InstituteOverviewDto getInstituteById(Long id);

    InstituteDto getDetailedInstitute(Long id);

    InstituteDto createInstitute(InstituteCreationDto dto);

    InstituteDto updateInstitute(Long id, InstituteUpdateDto dto);

    InstituteDto patchInstitute(Long id, InstituteUpdateDto dto);

    void deleteInstitute(Long id);

    Page<BranchOverviewDto> getBranchesByInstituteId(Long instituteId, Pageable pageable);


}
