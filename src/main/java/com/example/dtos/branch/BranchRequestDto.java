package com.example.dtos.branch;

import com.example.dtos.address.AddressRequestDto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class BranchRequestDto {

    private Long customerId;
    private String branchCode;
    private String locationCode;
    private String name;
    private String email;
    private AddressRequestDto address;
    private String telephoneNumber;
    private Long bankId;
}
