package com.develhope.spring.features.vehicle;

import com.develhope.spring.features.vehicle.dto.CreateVehicleRequest;
import com.develhope.spring.features.vehicle.dto.VehicleResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehicleMapper {
    private final ModelMapper modelMapper;

    public VehicleModel convertVehicleRequestToModel(CreateVehicleRequest createVehicleRequest) {
        return modelMapper.map(createVehicleRequest, VehicleModel.class);
    }

    public VehicleEntity convertVehicleModelToEntity(VehicleModel vehicleModel) {
        return modelMapper.map(vehicleModel, VehicleEntity.class);
    }

    public VehicleModel convertVehicleEntityToModel(VehicleEntity vehicleEntity) {
        return modelMapper.map(vehicleEntity, VehicleModel.class);
    }

    public VehicleResponse convertVehicleModelToResponse(VehicleModel vehicleModel) {
        return modelMapper.map(vehicleModel, VehicleResponse.class);
    }
}
