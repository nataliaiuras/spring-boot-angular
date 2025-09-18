package com.example.models.dtos.address;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class AddressDto  {

    private Long id;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String countryCode;
    private String addressType;

}