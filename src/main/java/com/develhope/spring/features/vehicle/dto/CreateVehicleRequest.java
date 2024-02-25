package com.develhope.spring.features.vehicle.dto;

import com.develhope.spring.features.vehicle.VehicleStatus;
import com.develhope.spring.features.vehicle.VehicleType;
import lombok.AllArgsConstructor;

import lombok.Value;

@Value
@AllArgsConstructor
public class CreateVehicleRequest {
    String model;
    String brand;
    Integer displacement;
    String color;
    Integer power;
    String shift;
    Integer yearOfMatriculation;
    String fuelType;
    Integer price;
    Integer discount;
    String accessories;
    Boolean used;
    VehicleStatus vehicleStatus;
    VehicleType vehicleType;
}