package com.develhope.spring.features.vehicle;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleEntity { //owner?
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Long id;
    @Column(nullable = false)
    @NotBlank
    private String model;
    @Column(nullable = false)
    @NotBlank
    private String brand;
    private Integer displacement;
    @Column(nullable = false)
    @NotBlank
    private String color;
    @Column(nullable = false)
    @NotBlank
    private Integer power;
    private String shift; //enum?
    @Column(nullable = false)
    @NotBlank
    private Integer yearOfMatriculation;
    @Column(nullable = false)
    @NotBlank
    private String fuelType;
    @Column(nullable = false)
    @NotBlank
    private Integer price;
    @Column(nullable = false)
    private Integer discount = 0;
    private String accessories;
    @Column(nullable = false)
    @NotBlank
    private Boolean used = false;
    @Enumerated(EnumType.STRING)
    private VehicleStatus vehicleStatus;
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
}

