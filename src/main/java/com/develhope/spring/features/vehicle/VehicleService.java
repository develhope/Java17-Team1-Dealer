package com.develhope.spring.features.vehicle;

import com.develhope.spring.features.vehicle.dto.CreateVehicleRequest;
import com.develhope.spring.features.vehicle.dto.VehicleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private VehicleMapper vehicleMapper;


    public VehicleResponse createVehicle(CreateVehicleRequest createVehicleRequest) {
        VehicleModel vehicleRequestModel = vehicleMapper.convertVehicleRequestToModel(createVehicleRequest);
        VehicleEntity vehicleRequestEntity = vehicleMapper.convertVehicleModelToEntity(vehicleRequestModel);
        VehicleEntity savedVehicleEntity = vehicleRepository.saveAndFlush(vehicleRequestEntity);
        VehicleModel vehicleResponseModel = vehicleMapper.convertVehicleEntityToModel(savedVehicleEntity);
        return vehicleMapper.convertVehicleModelToResponse(vehicleResponseModel);
    }

    public VehicleEntity getSingleVehicle(Long id) {
        Optional<VehicleEntity> user = vehicleRepository.findById(id);
        return user.orElse(null);
    }


    public VehicleResponse updateVehicle(long id, CreateVehicleRequest createVehicleRequest) {
        VehicleEntity vehicleEntityToUpdate = getSingleVehicle(id);
        if (vehicleEntityToUpdate != null) {
            VehicleModel vehicleRequestModel = vehicleMapper.convertVehicleRequestToModel(createVehicleRequest);
            vehicleEntityToUpdate.setModel(vehicleRequestModel.getModel());
            vehicleEntityToUpdate.setBrand(vehicleRequestModel.getBrand());
            vehicleEntityToUpdate.setDisplacement(vehicleRequestModel.getDisplacement());
            vehicleEntityToUpdate.setColor(vehicleRequestModel.getColor());
            vehicleEntityToUpdate.setPower(vehicleRequestModel.getPower());
            vehicleEntityToUpdate.setShift(vehicleRequestModel.getShift());
            vehicleEntityToUpdate.setYearOfmatriculation(vehicleRequestModel.getYearOfmatriculation());
            vehicleEntityToUpdate.setFuelType(vehicleRequestModel.getFuelType());
            vehicleEntityToUpdate.setPrice(vehicleRequestModel.getPrice());
            vehicleEntityToUpdate.setDiscount(vehicleRequestModel.getDiscount());
            vehicleEntityToUpdate.setAccesories(vehicleRequestModel.getAccesories());
            vehicleEntityToUpdate.setUsed(vehicleRequestModel.getUsed());
            vehicleEntityToUpdate.setVehicleStatus(vehicleRequestModel.getVehicleStatus());
            vehicleEntityToUpdate.setVehicleType(vehicleRequestModel.getVehicleType());
            VehicleEntity updatedVehicle = vehicleRepository.saveAndFlush(vehicleEntityToUpdate);
            VehicleModel vehicleResponseModel = vehicleMapper.convertVehicleEntityToModel(updatedVehicle);
            return vehicleMapper.convertVehicleModelToResponse(vehicleResponseModel);
        } else {
            return null;
        }
    }

    public Boolean deleteVehicle(long id) {
        vehicleRepository.deleteById(id);
        return !vehicleRepository.existsById(id);
    }

    public VehicleEntity updateVehicleStatusFromId(long id, String status) {
        Optional<VehicleEntity> vehicle = vehicleRepository.findById(id);
        if (vehicle.isPresent()) {
            String statusString = status.toUpperCase();
            VehicleStatus s = VehicleStatus.valueOf(statusString);
            vehicle.get().setVehicleStatus(s);
            return vehicleRepository.saveAndFlush(vehicle.get());
        } else {
            return null;
        }
    }

    public List<VehicleEntity> findByStatusAndUsed(String status, Boolean used) {
        String statusString = status.toUpperCase();
        VehicleStatus s = VehicleStatus.valueOf(statusString);
        return new ArrayList<>(vehicleRepository.findByVehicleStatusAndUsed(s, used));
    }

    public VehicleEntity getDetailsOfVehicle(long id) {
        return getSingleVehicle(id);
    }

    public List<VehicleEntity> getAllVehicles() {
        return vehicleRepository.findAll();
    }


    public Boolean deleteSingleVehicle(Long id) {
        if (vehicleRepository.existsById(id)) {
            vehicleRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
