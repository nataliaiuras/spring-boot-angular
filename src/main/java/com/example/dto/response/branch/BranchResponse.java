package com.example.dto.response.branch;

public record BranchResponse(Long id, String branchCode, String locationCode, String bicCode, String name, String email,
                             String phoneNumber, String lastModifiedDate) {
}