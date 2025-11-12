package com.example.dto.request.user;

import com.example.util.enums.Role;
import lombok.Getter;

@Getter
public class RoleUpdateRequest {
    private Role role;
}
