package com.example.dtos.user;

import com.example.utils.Role;

public record UserOverviewDto(Long id, String username, Role role) {
}
