package com.example.dtos.customer;

import com.example.dtos.account.AccountOverviewDto;
import com.example.dtos.address.AddressOverviewDto;
import com.example.dtos.branch.BranchOverviewDto;
import com.example.dtos.common.MetadataDto;
import com.example.dtos.user.UserOverviewDto;
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
    private String telephoneNumber;
    private String email;
    private AddressOverviewDto address;
    private UserOverviewDto user;
    private BranchOverviewDto branch;
    private Set<AccountOverviewDto> accounts = new HashSet<>();
    private MetadataDto metadata;

}