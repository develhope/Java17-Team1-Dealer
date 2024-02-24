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
        VehicleModel vehicleRequestModel = vehicleMapper.convertVehicleRequestToModel(createVehicleRequest);
        VehicleEntity vehicleRequestEntity = vehicleMapper.convertVehicleModelToEntity(vehicleRequestModel);
        VehicleEntity vehicleEntityToUpdate = getSingleVehicle(id);
        if (vehicleEntityToUpdate != null) {
            vehicleEntityToUpdate.setModel(vehicleRequestEntity.getModel());
            vehicleEntityToUpdate.setBrand(vehicleRequestEntity.getBrand());
            vehicleEntityToUpdate.setDisplacement(vehicleRequestEntity.getDisplacement());
            vehicleEntityToUpdate.setColor(vehicleRequestEntity.getColor());
            vehicleEntityToUpdate.setPower(vehicleRequestEntity.getPower());
            vehicleEntityToUpdate.setShift(vehicleRequestEntity.getShift());
            vehicleEntityToUpdate.setYearOfmatriculation(vehicleRequestEntity.getYearOfmatriculation());
            vehicleEntityToUpdate.setFuelType(vehicleRequestEntity.getFuelType());
            vehicleEntityToUpdate.setPrice(vehicleRequestEntity.getPrice());
            vehicleEntityToUpdate.setDiscount(vehicleRequestEntity.getDiscount());
            vehicleEntityToUpdate.setAccesories(vehicleRequestEntity.getAccesories());
            vehicleEntityToUpdate.setUsed(vehicleRequestEntity.getUsed());
            vehicleEntityToUpdate.setVehicleStatus(vehicleRequestModel.getVehicleStatus());
            vehicleEntityToUpdate.setVehicleType(vehicleRequestModel.getVehicleType());
            VehicleEntity updatedVehicle = vehicleRepository.saveAndFlush(vehicleEntityToUpdate);
            VehicleModel vehicleResponseModel = vehicleMapper.convertVehicleEntityToModel(updatedVehicle);
            return vehicleMapper.convertVehicleModelToResponse(vehicleResponseModel);
        }
        else {
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
        if (vehicleRepository.existsById(id)) {
            return vehicleRepository.findById(id).get();
        } else {
            return null;
        }
    }

    public List<VehicleEntity> getAllVehicles() {
        return vehicleRepository.findAll();
    }


    public void deleteSingleVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

}
