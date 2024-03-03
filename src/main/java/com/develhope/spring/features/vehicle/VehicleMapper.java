package com.develhope.spring.features.vehicle;

import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.users.UserMapper;
import com.develhope.spring.features.users.dto.UserResponse;
import com.develhope.spring.features.vehicle.dto.CreateVehicleRequest;
import com.develhope.spring.features.vehicle.dto.VehicleResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehicleMapper {
    private final ModelMapper modelMapper;
    private final UserMapper userMapper;

    //to fix mixmatch of properties
    public VehicleEntity convertCreateVehicleRequestToEntity(CreateVehicleRequest createVehicleRequest) {
        modelMapper.addMappings(new PropertyMap<CreateVehicleRequest, VehicleEntity>() {
            protected void configure() {
                skip().setSeller(null);
            }
        });

        return modelMapper.map(createVehicleRequest, VehicleEntity.class);
    }

    public VehicleResponse convertVehicleEntityToResponse(VehicleEntity vehicleEntity) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        VehicleResponse vehicleResponse = modelMapper.map(vehicleEntity, VehicleResponse.class);
        UserResponse seller = userMapper.convertUserEntityToResponse(vehicleEntity.getSeller());
        vehicleResponse.setSeller(seller);
        return vehicleResponse;
    }

    public VehicleResponse convertVehicleEntityToResponse(VehicleEntity vehicleEntity, UserResponse seller) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        VehicleResponse vehicleResponse = modelMapper.map(vehicleEntity, VehicleResponse.class);
        vehicleResponse.setSeller(seller);
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
