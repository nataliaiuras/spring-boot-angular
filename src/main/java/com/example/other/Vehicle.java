package com.example.other;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "VEHICLES")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String brand;

    private String model;

    private String color;

    @Column(name = "YEAR_PROD")
    private int yearProd;

}
