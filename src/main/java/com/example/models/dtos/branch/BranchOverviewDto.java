package com.example.models.dtos.branch;

import java.time.Instant;

public record BranchOverviewDto(
        Long id,
        String branchCode,
        String locationCode,
        String bicCode,
        String name,
        String email,
        String phoneNumber,
        Instant lastModifiedDate
) {
}