package com.develhope.spring.features.orders;

import com.develhope.spring.features.orders.dto.CreateOrderRequest;
import com.develhope.spring.features.orders.dto.OrderResponse;
import com.develhope.spring.features.users.UserMapper;

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
    private final UserMapper userMapper;

    public OrderEntity convertOrderRequestToEntity(CreateOrderRequest orderRequest) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(orderRequest, OrderEntity.class);
    }

    public OrderResponse convertOrderEntityToResponse(OrderEntity orderEntitySaved) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        OrderResponse orderResponse = modelMapper.map(orderEntitySaved, OrderResponse.class);
        orderResponse.setBuyer(userMapper.convertUserEntityToResponse(orderEntitySaved.getBuyer()));
        orderResponse.setSeller(userMapper.convertUserEntityToResponse(orderEntitySaved.getSeller()));
        return orderResponse;
    }

    <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
