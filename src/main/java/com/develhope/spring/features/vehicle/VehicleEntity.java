package com.develhope.spring.features.vehicle;

import com.develhope.spring.features.vehicle.PropertiesEnum.FuelType;
import com.develhope.spring.features.vehicle.PropertiesEnum.ShiftType;

import io.micrometer.common.lang.NonNullFields;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNullFields
public class VehicleEntity { //owner?
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Long id;
    @Column(nullable = false)
    @NotBlank
    private String model;
    @Column(nullable = false)
    @NotBlank
    private String brand;
    private Integer displacement;
    @Column(nullable = false)
    @NotBlank
    private String color;
    @Column(nullable = false)
    @NotBlank
    private Integer power;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShiftType shiftType;
    @Column(nullable = false)
    @NotBlank
    private Integer yearOfMatriculation;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType fuelType;
    @Column(nullable = false)
    @NotBlank
    private Integer price;
    @Column(nullable = false)
    private Integer discount = 0;
    private String accessories;
    @Column(nullable = false)
    @NotBlank
    private Boolean used = false;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus vehicleStatus;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;
}

