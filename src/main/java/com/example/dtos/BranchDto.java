package com.example.dtos;

import com.example.dtos.overview.AddressOverviewDto;
import com.example.dtos.overview.BankOverviewDto;
import com.example.dtos.overview.CustomerOverviewDto;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class BranchDto {

    private Long id;
    private String name;
    private String email;
    private AddressOverviewDto address;
    private String telephoneNumber;
    private String bicCode;
    private BankOverviewDto bank;
    private Set<CustomerOverviewDto> customers = new HashSet<>();

}