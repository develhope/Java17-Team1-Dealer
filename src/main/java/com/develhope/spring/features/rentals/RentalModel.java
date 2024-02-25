package com.develhope.spring.features.rentals;

import com.develhope.spring.features.orders.PaymentStatus;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.vehicle.VehicleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalModel {
    private Long id;
    private OffsetDateTime startOfRental;
    private OffsetDateTime endOfRental;
    private Float dailyCostRental;
    private Float totalCostRental;
    private PaymentStatus status;
    private VehicleEntity vehicleEntity;
    private UserEntity userEntity;
}


