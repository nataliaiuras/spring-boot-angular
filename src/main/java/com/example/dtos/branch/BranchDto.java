package com.example.dtos.branch;

import com.example.dtos.address.AddressDto;
import com.example.dtos.address.AddressOverviewDto;
import com.example.dtos.bank.BankOverviewDto;
import com.example.dtos.customer.CustomerRequestDto;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BranchDto {

    private Long id;

    private String branchCode;
    private String locationCode;
    private String bicCode;
    private String name;
    private String email;
    private String telephoneNumber;
    private AddressDto address;
    private BankOverviewDto bank;
    private Set<CustomerRequestDto> customers = new HashSet<>();

}