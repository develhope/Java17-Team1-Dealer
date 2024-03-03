package com.develhope.spring.features.orders;

import com.develhope.spring.features.orders.dto.CreateOrderRequest;
import com.develhope.spring.features.orders.dto.OrderResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final ModelMapper modelMapper;

    public OrderModel convertOrderRequestToModel(CreateOrderRequest orderRequest) {
        return modelMapper.map(orderRequest, OrderModel.class);
    }

    public OrderEntity convertOrderRequestToEntity(CreateOrderRequest orderRequest) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
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

    <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
