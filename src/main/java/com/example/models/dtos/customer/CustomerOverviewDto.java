package com.example.models.dtos.customer;

import java.time.LocalDate;

public record CustomerOverviewDto(
        Long id,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String cnp,
        String email
) {
}
