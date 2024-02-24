package com.develhope.spring.features.vehicle.dto;

import com.develhope.spring.features.vehicle.VehicleStatus;
import com.develhope.spring.features.vehicle.VehicleType;
import lombok.AllArgsConstructor;

import lombok.Value;

@Value
@AllArgsConstructor
public class CreateUserRequest {
    String model;
    String brand;
    Integer displacement;
    String color;
    Integer power;
    String shift;
    Integer yearOfmatriculation;
    String fuelType;
    Integer price;
    Integer discount;
    String accesories;
    Boolean used;

    VehicleStatus vehicleStatus;

    VehicleType vehicleType;
}