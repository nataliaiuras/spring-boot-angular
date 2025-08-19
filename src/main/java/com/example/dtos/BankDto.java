package com.example.dtos;

import com.example.dtos.overview.BranchOverviewDto;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class BankDto {

    private Long id;
    private String name;
    private String website;
    private Set<BranchOverviewDto> branches = new HashSet<>();

}
