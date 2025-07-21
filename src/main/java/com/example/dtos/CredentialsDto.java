package com.example.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class CredentialsDto {

    private Long id;
    private String username;
    private String password;
}
