package com.develhope.spring.features.rentals.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateRentalRequest {
    private OffsetDateTime startOfRental;
    private OffsetDateTime endOfRental;
    private Integer dailyCostRental;
    private Integer totalCostRental;
}
