package com.develhope.spring.entities.vehicle;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Long id;
    private String Model;
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
    @Enumerated
    private VehicleStatus status;
}

