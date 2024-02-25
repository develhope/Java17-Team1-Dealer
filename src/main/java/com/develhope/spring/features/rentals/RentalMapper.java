package com.develhope.spring.features.rentals;

import com.develhope.spring.features.rentals.dto.CreateRentalRequest;
import com.develhope.spring.features.rentals.dto.RentalResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RentalMapper {
    private final ModelMapper modelMapper;

    public RentalModel convertRentalRequestToModel(CreateRentalRequest createRentalRequest) {
        return modelMapper.map(createRentalRequest, RentalModel.class);
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
}