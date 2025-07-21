package com.example.dtos.request;


import com.example.utils.PasswordMatches;
import com.example.utils.Role;
import com.example.utils.ValidEmail;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class RegisterRequestDto {

    @NotNull
    @NotEmpty
    private String username;

   /* @ValidEmail
    @NotNull
    @NotEmpty*/
    private String email;

   /* @NotNull
    @NotEmpty*/
    private String password;
  /*  private String matchingPassword;*/

    private Role role;
}