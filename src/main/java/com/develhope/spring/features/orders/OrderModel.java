package com.develhope.spring.features.orders;


import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.vehicle.VehicleEntity;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

class OrderModel {

    private Long id;
    private Float deposit;
    private PaymentStatus paymentStatus;
    private OrderStatus orderStatus;
    private VehicleEntity vehicleEntity;
    private UserEntity userEntity;
}
