package com.example.models.dtos.branch;

import com.example.models.dtos.address.AddressRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class BranchRequestDto {

    @NotNull(message = "Branch code is required")
    @Pattern(regexp = "^[A-Z0-9]{3}$")
    private String branchCode;

    @NotNull(message = "Location code is required")
    @Pattern(regexp = "^\\d{2}$")
    private String locationCode;

    @NotNull(message = "Branch name is required")
    @Size(min = 2, max = 100, message = "Branch name must be between 2 and 100 characters")
    private String name;

    @Email(message = "Email must be valid")
    private String email;

    @NotNull(message = "Address is required")
    @Valid
    private AddressRequestDto address;

    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in international format")
    private String phoneNumber;

    @NotNull(message = "Institute id is required")
    @Valid
    private Long instituteId;
}
