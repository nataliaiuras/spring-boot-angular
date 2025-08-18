package com.example.dtos;

import com.example.models.Account;
import com.example.models.Branch;
import com.example.models.User;
import lombok.*;

import java.time.Instant;
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
    private User user;
    private Branch branch;
    private Set<Account> accounts = new HashSet<>();
    private Instant createdDate;
    private Instant lastModifiedDate;
    private Long version;
}
