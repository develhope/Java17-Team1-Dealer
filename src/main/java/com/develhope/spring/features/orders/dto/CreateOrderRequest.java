package com.develhope.spring.features.orders.dto;

import com.develhope.spring.features.orders.OrderStatus;
import com.develhope.spring.features.orders.PaymentStatus;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.vehicle.VehicleEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    Integer deposit;
}
