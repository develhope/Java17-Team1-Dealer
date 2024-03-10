package com.develhope.spring.features.vehicle.dto;

import com.develhope.spring.features.vehicle.PropertiesEnum.FuelType;
import com.develhope.spring.features.vehicle.PropertiesEnum.ShiftType;
import com.develhope.spring.features.vehicle.VehicleStatus;
import com.develhope.spring.features.vehicle.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponse {
    private Long id;
    private String model;
    private String brand;
    private Integer displacement;
    private String color;
    private Integer power;
    private ShiftType shift;
    private Integer yearOfMatriculation;
    private FuelType fuelType;
    private Long price;
    private Integer discount;
    private String accessories;
    private Boolean used;
    private VehicleStatus vehicleStatus;
    private VehicleType vehicleType;
}
