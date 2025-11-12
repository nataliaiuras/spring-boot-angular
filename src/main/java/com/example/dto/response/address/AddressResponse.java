package com.example.dto.response.address;

public record AddressResponse(Long id, String addressLine1, String city, String postalCode, String countryCode,
                              String addressType) {
}