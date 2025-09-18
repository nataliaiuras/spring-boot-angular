package com.example.utils.mapers;

import com.example.models.dtos.institute.InstituteCreationDto;
import com.example.models.dtos.institute.InstituteDto;
import com.example.models.dtos.institute.InstituteOverviewDto;
import com.example.models.dtos.institute.InstituteUpdateDto;
import com.example.models.entities.Institute;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InstituteMapper {

    InstituteDto toInstituteDto(Institute institute);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Institute toInstitute(@Valid InstituteDto dto);

    Institute toInstitute(@Valid InstituteCreationDto dto);

    Institute toInstitute(@Valid InstituteUpdateDto dto);

    InstituteOverviewDto toInstituteOverviewDto(Institute institute);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void updateInstitute(@MappingTarget Institute target, Institute source);

}