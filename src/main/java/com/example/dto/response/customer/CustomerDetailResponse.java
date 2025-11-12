package com.example.dto.response.customer;


import com.example.dto.response.account.AccountResponse;
import com.example.dto.response.address.AddressResponse;
import com.example.dto.response.branch.BranchResponse;
import com.example.dto.response.user.UserResponse;

import java.time.LocalDate;
import java.util.Set;

public record CustomerDetailResponse(Long id, String firstName, String lastName, LocalDate birthDate, String cnp,
                                     String phoneNumber, String email, AddressResponse address, UserResponse user,
                                     BranchResponse branch, boolean active, Set<AccountResponse> accounts,
                                     String lastModifiedDate, String createdDate, Long version) {
}