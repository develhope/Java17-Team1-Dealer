package com.develhope.spring.features.orders.dto;

import com.develhope.spring.features.vehicle.VehicleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchOrderRequest {
    private Long deposit;
    private String paymentStatus;
    private String orderStatus;
    private VehicleEntity vehicleEntity;
}
