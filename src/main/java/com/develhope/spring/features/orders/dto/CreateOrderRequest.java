package com.develhope.spring.features.orders.dto;

import com.develhope.spring.features.users.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    Integer deposit;
}
