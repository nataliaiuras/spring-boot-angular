package com.example.dto.request.address;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class AddressUpdateRequest {

    @Size(min = 2, max = 100, message = "First address line must be between 2 and 100 characters")
    private String addressLine1;

    @Size(min = 2, max = 100, message = "Second address line must be between 2 and 100 characters")
    private String addressLine2;

    @Size(min = 2, max = 100, message = "City must be between 2 and 100 characters")
    private String city;

    @Size(min = 2, max = 100, message = "State/Province must be between 2 and 100 characters")
    private String stateProvince;

    @Pattern(regexp = "^\\d{4,10}$", message = "Code must have a numeric format between 4-10 digits")
    private String postalCode;

    @Pattern(regexp = "^[A-Z]{2}$", message = "Code must contain two uppercase letters")
    private String countryCode;

}
