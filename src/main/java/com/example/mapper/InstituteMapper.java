package com.example.mapper;

import com.example.dto.request.institute.InstituteCreateRequest;
import com.example.dto.response.institute.InstituteDetailResponse;
import com.example.dto.response.institute.InstituteResponse;
import com.example.entity.Institute;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InstituteMapper {

    InstituteResponse toInstituteResponse(Institute institute);

    InstituteDetailResponse toInstituteDetailResponse(Institute institute);

    Institute toInstitute(InstituteCreateRequest instituteCreateRequest);
}