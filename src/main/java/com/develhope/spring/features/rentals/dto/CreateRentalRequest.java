package com.develhope.spring.features.rentals.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRentalRequest {
    Long vehicleId;
    Long sellerId;
    Long customerId;
    private OffsetDateTime startOfRental;
    private OffsetDateTime endOfRental;
}
