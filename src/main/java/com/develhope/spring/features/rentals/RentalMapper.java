package com.develhope.spring.features.rentals;

import com.develhope.spring.features.rentals.dto.CreateRentalRequest;
import com.develhope.spring.features.rentals.dto.RentalResponse;
import com.develhope.spring.features.users.UserMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RentalMapper {
    private final ModelMapper modelMapper;
    private final UserMapper userMapper;

    public RentalEntity convertCreateRentalRequestToEntity(CreateRentalRequest createRentalRequest) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(createRentalRequest, RentalEntity.class);
    }

    public RentalResponse convertRentalEntityToResponse(RentalEntity rentalEntity) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        RentalResponse rentalResponse = modelMapper.map(rentalEntity, RentalResponse.class);
        rentalResponse.setRenter(userMapper.convertUserEntityToResponse(rentalEntity.getRenter()));
        rentalResponse.setSeller(userMapper.convertUserEntityToResponse(rentalEntity.getSeller()));
        return rentalResponse;
    }

    <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}