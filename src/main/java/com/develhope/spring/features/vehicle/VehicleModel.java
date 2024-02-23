package com.develhope.spring.features.vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleModel {
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

    private VehicleStatus vehicleStatus;

    private VehicleType vehicleType;
}