package com.develhope.spring.features.vehicle;

import jakarta.persistence.*;
import lombok.*;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private long id;
    private String model;
    private String brand;
    private Integer displacement;
    private String color;
    private Integer power;
    private String shift;
    private Integer yearOfmatriculation;
    private String fuelType;
    private Integer price;
    private Integer discount;
    private String accesories;
    private Boolean used;
    @Enumerated(EnumType.STRING)
    private VehicleStatus vehicleStatus;
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
}

