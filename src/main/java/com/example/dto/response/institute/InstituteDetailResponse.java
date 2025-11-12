package com.example.dto.response.institute;

import com.example.dto.response.branch.BranchResponse;

import java.util.Set;

public record InstituteDetailResponse(Long id, String bankCode, String name, String website, Set<BranchResponse> branches,
                                      String createdDate, String lastModifiedDate, Long version) {
}