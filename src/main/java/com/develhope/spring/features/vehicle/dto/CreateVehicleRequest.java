package com.develhope.spring.features.vehicle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateVehicleRequest {
    private String model;
    private String brand;
    private Integer displacement;
    private String color;
    private Integer power;
    private String shiftType;
    private Integer yearOfMatriculation;
    private String fuelType;
    private Long price;
    private Integer discount;
    private String accessories;
    private Boolean used;
    private String vehicleStatus;
    private String vehicleType;
}