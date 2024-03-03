package com.develhope.spring.features.vehicle.dto;

import com.develhope.spring.features.orders.dto.OrderResponse;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.users.dto.UserResponse;
import com.develhope.spring.features.vehicle.VehicleStatus;
import com.develhope.spring.features.vehicle.VehicleType;
import com.develhope.spring.features.vehicle.PropertiesEnum.FuelType;
import com.develhope.spring.features.vehicle.PropertiesEnum.ShiftType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponse {
    Long id;
    String model;
    String brand;
    Integer displacement;
    String color;
    Integer power;
    ShiftType shift;
    Integer yearOfMatriculation;
    FuelType fuelType;
    Integer price;
    Integer discount;
    String accessories;
    Boolean used;
    VehicleStatus vehicleStatus;
    VehicleType vehicleType;
    UserResponse seller;
}
