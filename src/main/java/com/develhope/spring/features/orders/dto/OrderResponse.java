package com.develhope.spring.features.orders.dto;

import com.develhope.spring.features.orders.OrderStatus;
import com.develhope.spring.features.orders.PaymentStatus;
import com.develhope.spring.features.users.dto.UserResponse;
import com.develhope.spring.features.vehicle.VehicleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderResponse {
    private Long id;
    private Integer deposit;
    private PaymentStatus paymentStatus;
    private OrderStatus orderStatus;
    private VehicleEntity vehicleEntity;
    private UserResponse buyer;
    private UserResponse seller;
}
