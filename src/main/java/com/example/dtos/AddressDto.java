package com.example.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class AddressDto {

    private Long id;

    private int number;

    private String street;

    private String city;

    private String county;

    private String country;

    private int postalCode;
}
