package com.example.models.dtos.user;

import com.example.models.dtos.customer.CustomerOverviewDto;
import com.example.utils.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {

    private Long id;
    private String username;
    private Role role;
    private boolean enabled;
    private CustomerOverviewDto customer;
    private Instant createdDate;
    private Instant lastModifiedDate;
    private Long version;

}