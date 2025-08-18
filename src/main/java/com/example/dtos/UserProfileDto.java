package com.example.dtos;

import com.example.utils.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserProfileDto {

    private String username;
    private Role role;

}