package com.develhope.spring.features.rentals.dto;

import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.vehicle.VehicleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchRentalRequest {
    private OffsetDateTime startOfRental;
    private OffsetDateTime endOfRental;
    private Long dailyCostRental;
    private Long totalCostRental;
    private String paymentStatus;
    private VehicleEntity vehicleEntity;
    private UserEntity userEntity;
}
