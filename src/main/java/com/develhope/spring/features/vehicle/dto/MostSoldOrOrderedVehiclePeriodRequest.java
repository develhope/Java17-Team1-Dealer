package com.develhope.spring.features.vehicle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MostSoldOrOrderedVehiclePeriodRequest {
    String startDate;
    String endDate;
}
