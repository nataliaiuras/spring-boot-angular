package com.example.models.dtos.user;

import com.example.utils.enums.Role;

public record UserOverviewDto(Long id, String username, Role role) {
}
