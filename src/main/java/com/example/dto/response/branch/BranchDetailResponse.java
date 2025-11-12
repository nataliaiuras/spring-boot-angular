package com.example.dto.response.branch;

import com.example.dto.response.address.AddressDetailResponse;
import com.example.dto.response.customer.CustomerResponse;
import com.example.dto.response.institute.InstituteResponse;

import java.util.Set;

public record BranchDetailResponse(Long id, String branchCode, String locationCode, String bicCode, String name,
                                   String email, String phoneNumber, AddressDetailResponse address,
                                   InstituteResponse institute, Set<CustomerResponse> customers,
                                   String lastModifiedDate, String createdDate, Long version) {
}