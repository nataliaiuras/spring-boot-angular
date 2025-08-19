package com.example.dtos.overview;

import java.util.Date;

public record CustomerOverviewDto(long id, String firstName, String lastName, Date birthDate, String cnp,
                                  String telephoneNumber, String email) {
}
