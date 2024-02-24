package com.develhope.spring.features.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    @Autowired
    private  VehicleRepository vehicleRepository;
    @Autowired
    private  VehicleMapper vehicleMapper;


    public VehicleEntity createVehicle(VehicleEntity vehicleEntity) {
        return vehicleRepository.saveAndFlush(vehicleEntity);
    }

    public VehicleEntity getSingleVehicle(Long id) {
        Optional<VehicleEntity> user = vehicleRepository.findById(id);
        return user.orElse(null);
    }


    public VehicleEntity updateVehicle(long id, VehicleEntity vehicleEntity) {
        VehicleEntity vehicleEntityToUpdate = getSingleVehicle(id);
        vehicleEntityToUpdate.setModel(vehicleEntity.getModel());
        vehicleEntityToUpdate.setBrand(vehicleEntity.getBrand());
        vehicleEntityToUpdate.setDisplacement(vehicleEntity.getDisplacement());
        vehicleEntityToUpdate.setColor(vehicleEntity.getColor());
        vehicleEntityToUpdate.setPower(vehicleEntity.getPower());
        vehicleEntityToUpdate.setShift(vehicleEntity.getShift());
        vehicleEntityToUpdate.setYearOfmatriculation(vehicleEntity.getYearOfmatriculation());
        vehicleEntityToUpdate.setFuelType(vehicleEntity.getFuelType());
        vehicleEntityToUpdate.setPrice(vehicleEntity.getPrice());
        vehicleEntityToUpdate.setDiscount(vehicleEntity.getDiscount());
        vehicleEntityToUpdate.setAccesories(vehicleEntity.getAccesories());
        vehicleEntityToUpdate.setUsed(vehicleEntity.getUsed());
        vehicleEntityToUpdate.setVehicleStatus(vehicleEntity.getVehicleStatus());
        vehicleEntityToUpdate.setVehicleType(vehicleEntity.getVehicleType());
        vehicleRepository.saveAndFlush(vehicleEntityToUpdate);
        return vehicleEntityToUpdate;
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
