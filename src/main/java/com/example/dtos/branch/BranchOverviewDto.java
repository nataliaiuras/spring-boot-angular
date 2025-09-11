package com.example.dtos.branch;

public record BranchOverviewDto(
        Long id,
        String branchCode,
        String locationCode,
        String bicCode,
        String name,
        String email,
        String telephoneNumber
) {
}