package com.example.dtos.user;

import com.example.dtos.customer.CustomerOverviewDto;
import com.example.utils.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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

}