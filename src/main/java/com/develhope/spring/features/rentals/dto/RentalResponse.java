package com.develhope.spring.features.rentals.dto;

import com.develhope.spring.features.orders.PaymentStatus;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.vehicle.VehicleEntity;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.OffsetDateTime;

@Value
@AllArgsConstructor
public class RentalResponse {
    private Long id;
    private OffsetDateTime startOfRental;
    private OffsetDateTime endOfRental;
    private Float dailyCostRental;
    private Float totalCostRental;
    private PaymentStatus status;
    private VehicleEntity vehicleEntity;
    private UserEntity userEntity;
}
