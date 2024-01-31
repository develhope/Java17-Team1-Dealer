package com.develhope.spring.Vehicles;

import lombok.Data;

@Data
public class Vehicle {
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
    private VehicleStatusEnum status;
}

