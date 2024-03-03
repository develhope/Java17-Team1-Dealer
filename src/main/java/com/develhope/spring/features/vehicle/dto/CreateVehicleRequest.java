package com.develhope.spring.features.vehicle.dto;

import com.develhope.spring.features.vehicle.VehicleStatus;
import com.develhope.spring.features.vehicle.VehicleType;
import com.develhope.spring.features.vehicle.PropertiesEnum.ShiftType;

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
    Integer price;
    Integer discount;
    String accessories;
    Boolean used;
    String vehicleStatus;
    String vehicleType;
}