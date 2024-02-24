package com.develhope.spring.features.vehicle;

import com.develhope.spring.features.vehicle.dto.CreateUserRequest;
import com.develhope.spring.features.vehicle.dto.VehicleResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class VehicleMapper {
    @Autowired
    ModelMapper modelMapper;

    public VehicleModel convertVehicleRequestToModel (CreateUserRequest createVehicleRequest){
        return  modelMapper.map(createVehicleRequest,VehicleModel.class);
    }
    public VehicleEntity convertVehicleModelToEntity (VehicleModel vehicleModel){
        return  modelMapper.map(vehicleModel,VehicleEntity.class);
    }
    public VehicleModel convertVehicleEntityToModel (VehicleEntity vehicleEntity){
        return modelMapper.map(vehicleEntity, VehicleModel.class);
    }
    public VehicleResponse convertVehicleModelToResponse (VehicleModel vehicleModel){
        return modelMapper.map(vehicleModel, VehicleResponse.class);
    }
}
