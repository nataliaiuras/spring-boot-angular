package com.example.controller;

import com.example.dto.request.institute.InstituteCreateRequest;
import com.example.dto.request.institute.InstituteUpdateRequest;
import com.example.dto.response.branch.BranchResponse;
import com.example.dto.response.institute.InstituteDetailResponse;
import com.example.dto.response.institute.InstituteResponse;
import com.example.service.InstituteService;
import com.example.dto.response.general.ApiResponse;
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
    public ResponseEntity<ApiResponse<Page<InstituteResponse>>> getAllInstitutes(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<InstituteResponse> instituteResponsePage = instituteService.getAllInstitutes(pageable);
        return ResponseEntity.ok(ApiResponse.success(instituteResponsePage));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<InstituteResponse>> getInstitute(@PathVariable Long id) {
        InstituteResponse instituteResponse = instituteService.getInstituteById(id);
        return ResponseEntity.ok(ApiResponse.success(instituteResponse));
    }

    @GetMapping("{id}/details")
    public ResponseEntity<ApiResponse<InstituteDetailResponse>> getDetailedInstitute(@PathVariable Long id) {
        InstituteDetailResponse instituteDetailResponse = instituteService.getDetailedInstitute(id);
        return ResponseEntity.ok(ApiResponse.success(instituteDetailResponse));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InstituteDetailResponse>> createInstitute(
            @Valid @RequestBody InstituteCreateRequest instituteCreateRequest) {
        InstituteDetailResponse instituteDetailResponse = instituteService.createInstitute(instituteCreateRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(instituteDetailResponse.id()).toUri();
        return ResponseEntity.created(location)
                .body(ApiResponse.success(instituteDetailResponse, "Institute created successfully"));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<InstituteDetailResponse>> updateInstitute(
            @PathVariable Long id,
            @Valid @RequestBody InstituteUpdateRequest instituteUpdateRequest) {
        InstituteDetailResponse instituteDetailResponse = instituteService.updateInstitute(id, instituteUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(instituteDetailResponse, "Institute updated successfully"));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<InstituteDetailResponse>> patchInstitute(
            @PathVariable Long id,
            @RequestBody InstituteUpdateRequest instituteUpdateRequest) {
        InstituteDetailResponse instituteDetailResponse = instituteService.patchInstitute(id, instituteUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(instituteDetailResponse, "Institute updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteInstitute(@PathVariable Long id) {
        instituteService.deleteInstitute(id);
        return ResponseEntity.ok(ApiResponse.success("Institute deleted successfully"));
    }

    @GetMapping("{id}/branches")
    public ResponseEntity<ApiResponse<Page<BranchResponse>>> getBranches(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<BranchResponse> branchResponsePage = instituteService.getBranchesByInstituteId(id, pageable);
        return ResponseEntity.ok(ApiResponse.success(branchResponsePage));
    }


}
