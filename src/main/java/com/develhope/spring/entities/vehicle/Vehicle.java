package com.develhope.spring.entities.vehicle;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private long id;
    @NotBlank
    @Column(nullable = false)
    private String model;
    @NotBlank
    @Column(nullable = false)
    private String brand;
    private Integer displacement;
    @NotBlank
    @Column(nullable = false)
    private String color;
    @NotBlank
    @Column(nullable = false)
    private Integer power;
    @NotBlank
    @Column(nullable = false)
    private String shift;
    @NotBlank
    @Column(nullable = false)
    private Integer yearOfMatriculation;
    @NotBlank
    @Column(nullable = false)
    private String fuelType;
    @NotBlank
    @Column(nullable = false)
    private Integer price;
    @NotBlank
    @Column(nullable = false)
    private Integer discount;
    @NotBlank
    @Column(nullable = false)
    private String accessories;
    @NotBlank
    @Column(nullable = false)
    private Boolean used = false;
    @Column(nullable = false)
    @Enumerated
    @NotBlank
    private VehicleStatus vehicleStatus = VehicleStatus.valueOf("PROMPT_DELIVERY");
    @Column(nullable = false)
    @Enumerated
    private VehicleType vehicleType = VehicleType.valueOf("CAR");
}

