package com.develhope.spring.features.orders.dto;

import com.develhope.spring.features.vehicle.VehicleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchOrderRequest {
    Long deposit;
    String paymentStatus;
    String orderStatus;
    VehicleEntity vehicleEntity;
}
