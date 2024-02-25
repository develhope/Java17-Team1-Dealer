package com.develhope.spring.features.orders;

import com.develhope.spring.features.orders.dto.CreateOrderRequest;
import com.develhope.spring.features.orders.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final ModelMapper modelMapper;

    public OrderModel convertOrderRequestToModel(CreateOrderRequest createOrderRequest) {
        return modelMapper.map(createOrderRequest, OrderModel.class);
    }

    public OrderEntity convertOrderModelToEntity(OrderModel orderModel) {
        return modelMapper.map(orderModel, OrderEntity.class);
    }

    public OrderModel convertOrderEntityToModel(OrderEntity orderEntity) {
        return modelMapper.map(orderEntity, OrderModel.class);
    }

    public OrderResponse convertOrderModelToResponse(OrderModel orderModel) {
        return modelMapper.map(orderModel, OrderResponse.class);
    }
}
