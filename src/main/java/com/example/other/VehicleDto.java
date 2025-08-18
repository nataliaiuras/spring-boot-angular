package com.example.other;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class VehicleDto {

    @NonNull
    private Long id;

    @NotNull
    private String brand;

    @NotNull
    private String model;

    @NotNull
    private String color;

    @NotNull
    private int yearProd;


}
