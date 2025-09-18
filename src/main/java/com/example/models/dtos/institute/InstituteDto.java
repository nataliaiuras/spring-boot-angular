package com.example.models.dtos.institute;

import com.example.models.dtos.branch.BranchOverviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class InstituteDto {

    private Long id;
    private String bankCode;
    private String name;
    private String website;
    private Set<BranchOverviewDto> branches = new HashSet<>();
    private Instant createdDate;
    private Instant lastModifiedDate;
    private Long version;

}