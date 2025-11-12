package com.example.dto.response.user;

import com.example.util.enums.Role;

public record UserResponse(Long id, String username, Role role) {
}
