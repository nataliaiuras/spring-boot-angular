package com.example.models.dtos.branch;

import com.example.models.dtos.address.AddressDto;
import com.example.models.dtos.institute.InstituteOverviewDto;
import com.example.models.dtos.common.MetadataDto;
import com.example.models.dtos.customer.CustomerRequestDto;
import lombok.*;

import java.time.Instant;
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
    private String phoneNumber;
    private AddressDto address;
    private InstituteOverviewDto institute;
    private Set<CustomerRequestDto> customers = new HashSet<>();
    private Instant createdDate;
    private Instant lastModifiedDate;
    private Long version;

}