package com.example.models.dtos.customer;

import com.example.models.dtos.account.AccountOverviewDto;
import com.example.models.dtos.address.AddressOverviewDto;
import com.example.models.dtos.branch.BranchOverviewDto;
import com.example.models.dtos.common.MetadataDto;
import com.example.models.dtos.user.UserOverviewDto;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CustomerDto {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String cnp;
    private String phoneNumber;
    private String email;
    private AddressOverviewDto address;
    private UserOverviewDto user;
    private BranchOverviewDto branch;
    private Set<AccountOverviewDto> accounts = new HashSet<>();
    private MetadataDto metadata;

}