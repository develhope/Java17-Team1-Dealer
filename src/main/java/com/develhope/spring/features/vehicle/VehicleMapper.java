package com.develhope.spring.features.vehicle;

import com.develhope.spring.features.users.dto.UserResponse;
import com.develhope.spring.features.vehicle.dto.CreateVehicleRequest;
import com.develhope.spring.features.vehicle.dto.VehicleResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VehicleMapper {
    private final ModelMapper modelMapper;

    //to fix mixmatch of properties
    public VehicleEntity convertCreateVehicleRequestToEntity(CreateVehicleRequest createVehicleRequest) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(createVehicleRequest, VehicleEntity.class);
    }

    public VehicleResponse convertVehicleEntityToResponse(VehicleEntity vehicleEntity) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        VehicleResponse vehicleResponse = modelMapper.map(vehicleEntity, VehicleResponse.class);
        return vehicleResponse;
    }

    public VehicleResponse convertVehicleEntityToResponse(VehicleEntity vehicleEntity, UserResponse seller) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        VehicleResponse vehicleResponse = modelMapper.map(vehicleEntity, VehicleResponse.class);
        return vehicleResponse;
    }

    <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

}
