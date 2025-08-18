package com.example.dtos;

import com.example.models.Branch;
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
public class BankDto {

    private Long id;
    private String name;
    private String website;
    private Set<Branch> branches = new HashSet<>();
    private Instant createdDate;
    private Instant lastModifiedDate;
    private Long version;

}
