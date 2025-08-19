package com.example.dtos.overview;

public record AddressOverviewDto(Long id, int number, String street, String city, String county, String country,
                                 String postalCode) {
}
