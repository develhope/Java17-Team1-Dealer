package com.develhope.spring.features.vehicle;

import com.develhope.spring.features.vehicle.dto.CreateVehicleRequest;
import com.develhope.spring.features.vehicle.dto.VehicleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
            vehicleEntityToUpdate.setYearOfMatriculation(vehicleRequestModel.getYearOfMatriculation());
            vehicleEntityToUpdate.setFuelType(vehicleRequestModel.getFuelType());
            vehicleEntityToUpdate.setPrice(vehicleRequestModel.getPrice());
            vehicleEntityToUpdate.setDiscount(vehicleRequestModel.getDiscount());
            vehicleEntityToUpdate.setAccessories(vehicleRequestModel.getAccessories());
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

    public ResponseEntity<?> findByStatusAndUsed(String status, Boolean used) {
        if(VehicleStatus.isValidVehicleStatus(status.toUpperCase())){
            VehicleStatus vehicleStatus = VehicleStatus.valueOf(status.toUpperCase());
            return new ResponseEntity<>(vehicleRepository.findByVehicleStatusAndUsed(vehicleStatus, used), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
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
