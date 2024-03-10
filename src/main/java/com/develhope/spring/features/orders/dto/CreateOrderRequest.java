package com.develhope.spring.features.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private Long vehicleId;
    private Long sellerId;
    private Long customerId;
    private Long deposit;
}
