package com.example.dtos;

import com.example.utils.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String username;
    private Role role;

}