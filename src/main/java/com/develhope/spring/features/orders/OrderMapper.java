package com.develhope.spring.features.orders;

import com.develhope.spring.features.orders.dto.CreateOrderRequest;
import com.develhope.spring.features.orders.dto.OrderResponse;
import com.develhope.spring.features.vehicle.VehicleEntity;
import com.develhope.spring.features.vehicle.VehicleModel;
import com.develhope.spring.features.vehicle.dto.CreateUserRequest;
import com.develhope.spring.features.vehicle.dto.VehicleResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderMapper {
    @Autowired
    ModelMapper modelMapper;

    public OrderModel convertOrderRequestToModel (CreateOrderRequest createOrderRequest){
        return  modelMapper.map(createOrderRequest,OrderModel.class);
    }
    public OrderEntity convertOrderModelToEntity (OrderModel orderModel){
        return  modelMapper.map(orderModel,OrderEntity.class);
    }
    public OrderModel convertOrderntityToModel (OrderEntity orderEntity){
        return modelMapper.map(orderEntity, OrderModel.class);
    }
    public OrderResponse convertOrderModelToResponse (OrderModel orderModel){
        return modelMapper.map(orderModel, OrderResponse.class);
    }
}
