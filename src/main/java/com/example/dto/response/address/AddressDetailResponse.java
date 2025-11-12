package com.example.dto.response.address;

public record AddressDetailResponse(Long id, String addressLine1, String addressLine2, String city,
                                    String stateProvince, String postalCode, String countryCode, String addressType,
                                    String createdDate, String lastModifiedDate, Long version) {
}