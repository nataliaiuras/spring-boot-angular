package com.example.dtos;

import com.example.dtos.overview.AccountOverviewDto;
import com.example.dtos.overview.BranchOverviewDto;
import com.example.dtos.overview.UserOverviewDto;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class CustomerDto {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String cnp;
    private String telephoneNumber;
    private String email;
    private UserOverviewDto user;
    private BranchOverviewDto branch;
    private Set<AccountOverviewDto> accounts = new HashSet<>();

}