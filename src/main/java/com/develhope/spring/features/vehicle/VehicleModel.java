package com.develhope.spring.features.vehicle;

import com.develhope.spring.features.vehicle.PropertiesEnum.FuelType;
import com.develhope.spring.features.vehicle.PropertiesEnum.ShiftType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleModel {
    String model;
    String brand;
    Integer displacement;
    String color;
    Integer power;
    ShiftType shiftType;
    Integer yearOfMatriculation;
    FuelType fuelType;
    Integer price;
    Integer discount;
    String accessories;
    Boolean used;
    VehicleStatus vehicleStatus;
    VehicleType vehicleType;
}