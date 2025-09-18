package com.example.controllers;

import com.example.exceptions.response.ApiResponse;
import com.example.models.dtos.institute.InstituteCreationDto;
import com.example.models.dtos.institute.InstituteDto;
import com.example.models.dtos.institute.InstituteOverviewDto;
import com.example.models.dtos.institute.InstituteUpdateDto;
import com.example.models.dtos.branch.BranchOverviewDto;
import com.example.services.InstituteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/institute")
@Validated
@RequiredArgsConstructor
@Slf4j
public class InstituteController {

    private final InstituteService instituteService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<InstituteOverviewDto>>> getAllInstitutes(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<InstituteOverviewDto> pagedInstitutes = instituteService.getAllInstitutes(pageable);
        return ResponseEntity.ok(ApiResponse.success(pagedInstitutes));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<InstituteOverviewDto>> getInstitute(@PathVariable Long id) {
        InstituteOverviewDto dto = instituteService.getInstituteById(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("{id}/details")
    public ResponseEntity<ApiResponse<InstituteDto>> getDetailedInstitute(@PathVariable Long id) {
        InstituteDto dto = instituteService.getDetailedInstitute(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InstituteDto>> createInstitute(@Valid @RequestBody InstituteCreationDto dto) {
        InstituteDto createdDto = instituteService.createInstitute(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdDto.getId()).toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(createdDto, "Institute created successfully"));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<InstituteDto>> updateInstitute(@PathVariable Long id,
                                                                @Valid @RequestBody InstituteUpdateDto dto) {
        InstituteDto updatedDto = instituteService.updateInstitute(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updatedDto, "Institute updated successfully"));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<InstituteDto>> patchInstitute(@PathVariable Long id,
                                                               @RequestBody InstituteUpdateDto dto) {
        InstituteDto updatedDto = instituteService.patchInstitute(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updatedDto, "Institute updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteInstitute(@PathVariable Long id) {
        instituteService.deleteInstitute(id);
        return ResponseEntity.ok(ApiResponse.success("Institute deleted successfully"));
    }

    @GetMapping("{id}/branches")
    public ResponseEntity<ApiResponse<Page<BranchOverviewDto>>> getBranches(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<BranchOverviewDto> pagedBranches = instituteService.getBranchesByInstituteId(id, pageable);
        return ResponseEntity.ok(ApiResponse.success(pagedBranches));
    }


}
