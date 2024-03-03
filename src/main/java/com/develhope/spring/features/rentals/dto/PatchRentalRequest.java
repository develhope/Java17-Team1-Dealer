package com.develhope.spring.features.rentals.dto;

import java.time.OffsetDateTime;

import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.vehicle.VehicleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchRentalRequest {
    private OffsetDateTime startOfRental;
    private OffsetDateTime endOfRental;
    private Integer dailyCostRental;
    private Integer totalCostRental;
    private String paymentStatus;
    private VehicleEntity vehicleEntity;
    private UserEntity userEntity;
}
