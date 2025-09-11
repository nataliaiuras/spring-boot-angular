package com.example.dtos.bank;

import com.example.dtos.address.AddressOverviewDto;
import com.example.dtos.branch.BranchOverviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BankDto {

    private Long id;
    private String bankCode;
    private String name;
    private String website;
    private AddressOverviewDto address;
    private Set<BranchOverviewDto> branches = new HashSet<>();

}