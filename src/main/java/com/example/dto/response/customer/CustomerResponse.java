package com.example.dto.response.customer;

import java.time.LocalDate;

public record CustomerResponse(Long id, String firstName, String lastName, LocalDate birthDate, String cnp,
                               String email) {
}
