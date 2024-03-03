package com.develhope.spring.features.rentals;

import com.develhope.spring.features.orders.OrderEntity;
import com.develhope.spring.features.orders.dto.CreateOrderRequest;
import com.develhope.spring.features.orders.dto.OrderResponse;
import com.develhope.spring.features.rentals.dto.CreateRentalRequest;
import com.develhope.spring.features.rentals.dto.PatchRentalRequest;
import com.develhope.spring.features.rentals.dto.RentalResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RentalMapper {
    private final ModelMapper modelMapper;

    public RentalModel convertRentalRequestToModel(CreateRentalRequest createRentalRequest) {
        return modelMapper.map(createRentalRequest, RentalModel.class);
    }

    public RentalEntity convertCreateRentalRequestToEntity(CreateRentalRequest createRentalRequest) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(createRentalRequest, RentalEntity.class);
    }

    public RentalEntity convertRentalModelToEntity(RentalModel rentalModel) {
        return modelMapper.map(rentalModel, RentalEntity.class);
    }

    public RentalModel convertRentalEntityToModel(RentalEntity rentalEntity) {
        return modelMapper.map(rentalEntity, RentalModel.class);
    }

    public RentalResponse convertRentalModelToResponse(RentalModel rentalModel) {
        return modelMapper.map(rentalModel, RentalResponse.class);
    }

    public RentalResponse convertRentalEntityToResponse(RentalEntity rentalEntity) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(rentalEntity, RentalResponse.class);
    }

    <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}