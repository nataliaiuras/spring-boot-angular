package com.example.dto.request.branch;

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
public class BranchUpdateRequest {

    @Pattern(regexp = "^[A-Z0-9]{3}$")
    private String branchCode;

    @Pattern(regexp = "^\\d{2}$")
    private String locationCode;

    @Size(min = 2, max = 100, message = "Branch name must be between 2 and 100 characters")
    private String name;

    @Email(message = "Email must be valid")
    private String email;

    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in international format")
    private String phoneNumber;

    @NotNull(message = "Institute id is required")
    @Valid
    private Long instituteId;
}
