package com.example.dtos.bank;

import com.example.dtos.address.AddressRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BankRequestDto  {

    @NotNull(message = "Bank code is required")
    @Pattern(regexp = "^[A-Z]{4}$", message = "Bank code must be 4 uppercase letters")
    private String bankCode;

    @NotNull(message = "Bank name is required")
    @Size(min = 2, max = 100, message = "Bank name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Website is required")
    @URL(message = "Website must be a valid URL")
    private String website;

    @NotNull(message = "Address is required")
    @Valid
    private AddressRequestDto address;


}
