package com.develhope.spring.features.rentals.dto;

import com.develhope.spring.features.orders.PaymentStatus;
import com.develhope.spring.features.users.dto.UserResponse;
import com.develhope.spring.features.vehicle.VehicleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalResponse {
    private Long id;
    private OffsetDateTime startOfRental;
    private OffsetDateTime endOfRental;
    private Integer dailyCostRental;
    private Integer totalCostRental;
    private PaymentStatus status;
    private VehicleEntity vehicleEntity;
    private UserResponse renter;
    private UserResponse seller;
}
