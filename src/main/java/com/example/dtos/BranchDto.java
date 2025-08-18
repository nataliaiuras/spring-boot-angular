package com.example.dtos;

import com.example.models.Address;
import com.example.models.Bank;
import com.example.models.Customer;
import lombok.*;

import java.time.Instant;
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
    private Address address;
    private String telephoneNumber;
    private String bicCode;
    private Bank bank;
    private Set<Customer> customers = new HashSet<>();
    private Instant createdDate;
    private Instant lastModifiedDate;
    private Long version;
}
