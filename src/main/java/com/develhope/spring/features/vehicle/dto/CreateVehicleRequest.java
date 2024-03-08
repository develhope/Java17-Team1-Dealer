package com.develhope.spring.features.vehicle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateVehicleRequest {
    String model;
    String brand;
    Integer displacement;
    String color;
    Integer power;
    String shiftType;
    Integer yearOfMatriculation;
    String fuelType;
    Long price;
    Integer discount;
    String accessories;
    Boolean used;
    String vehicleStatus;
    String vehicleType;
}