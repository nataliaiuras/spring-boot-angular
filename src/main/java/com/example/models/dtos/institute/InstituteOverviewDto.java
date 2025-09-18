package com.example.models.dtos.institute;

import java.time.Instant;

public record InstituteOverviewDto(Long id, String bankCode, String name, String website, Instant lastModifiedDate) {
}