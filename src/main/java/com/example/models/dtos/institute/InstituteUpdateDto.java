package com.example.models.dtos.institute;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class InstituteUpdateDto {

    @Pattern(regexp = "^[A-Z]{4}$", message = "Institute code must be 4 uppercase letters")
    private String bankCode;

    @Size(min = 2, max = 100, message = "Institute name must be between 2 and 100 characters")
    private String name;

    @URL(message = "Website must be a valid URL")
    private String website;


}
