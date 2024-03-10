package com.develhope.spring.features.vehicle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MostSoldOrOrderedVehiclePeriodRequest {
    private String startDate;
    private String endDate;
}
