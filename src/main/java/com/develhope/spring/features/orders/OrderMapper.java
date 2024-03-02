package com.develhope.spring.features.orders;

import com.develhope.spring.features.orders.dto.OrderRequest;
import com.develhope.spring.features.orders.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final ModelMapper modelMapper;

    public OrderModel convertOrderRequestToModel(OrderRequest orderRequest) {
        return modelMapper.map(orderRequest, OrderModel.class);
    }

    public OrderEntity convertOrderRequestByToEntity(OrderRequest orderRequest) {
        return modelMapper.map(orderRequest, OrderEntity.class);
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

    public OrderResponse convertOrderEntityToResponse(OrderEntity orderEntitySaved) {
        return modelMapper.map(orderEntitySaved, OrderResponse.class);
    }
}
