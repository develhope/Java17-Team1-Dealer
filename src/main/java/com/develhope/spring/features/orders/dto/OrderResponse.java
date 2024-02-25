package com.develhope.spring.features.orders.dto;

import com.develhope.spring.features.orders.OrderStatus;
import com.develhope.spring.features.orders.PaymentStatus;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.vehicle.VehicleEntity;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class OrderResponse {
    Long id;
    Float deposit;

    PaymentStatus paymentStatus;

    OrderStatus orderStatus;

    VehicleEntity vehicleEntity;


    UserEntity userEntity;
}