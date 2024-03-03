package com.develhope.spring.features.orders.dto;

import com.develhope.spring.features.orders.OrderStatus;
import com.develhope.spring.features.orders.PaymentStatus;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.vehicle.VehicleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderResponse {
    Long id;
    Integer deposit;
    PaymentStatus paymentStatus;
    OrderStatus orderStatus;
    VehicleEntity vehicleEntity;
    UserEntity buyer;
}
