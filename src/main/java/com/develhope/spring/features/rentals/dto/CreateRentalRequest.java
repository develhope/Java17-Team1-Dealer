package com.develhope.spring.features.rentals.dto;

import com.develhope.spring.features.orders.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.OffsetDateTime;

@Value
@AllArgsConstructor
public class CreateRentalRequest {
    private OffsetDateTime startOfRental;
    private OffsetDateTime endOfRental;
    private Integer dailyCostRental;
    private Integer totalCostRental;
    private PaymentStatus status;
}
