package com.example.dtos.overview;

import com.example.utils.Role;

public record UserOverviewDto(Long id, String username, Role role) {
}
