package com.develhope.spring.services;

import com.develhope.spring.entities.vehicle.Vehicle;
import com.develhope.spring.entities.vehicle.VehicleStatus;
import com.develhope.spring.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    @Autowired
    VehicleRepository vehicleRepository;

    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.saveAndFlush(vehicle);
    }

    public Vehicle updateVehicle(long id, Vehicle vehicle) {
        Optional<Vehicle> foundVehicle = vehicleRepository.findById(id);
        if (foundVehicle.isPresent()) {
            foundVehicle.get().setModel(vehicle.getModel());
            foundVehicle.get().setBrand(vehicle.getBrand());
            foundVehicle.get().setDisplacement(vehicle.getDisplacement());
            foundVehicle.get().setColor(vehicle.getColor());
            foundVehicle.get().setPower(vehicle.getPower());
            foundVehicle.get().setShift(vehicle.getShift());
            foundVehicle.get().setYearOfMatriculation(vehicle.getYearOfMatriculation());
            foundVehicle.get().setFuelType(vehicle.getFuelType());
            foundVehicle.get().setPrice(vehicle.getPrice());
            foundVehicle.get().setDiscount(vehicle.getDiscount());
            foundVehicle.get().setAccessories(vehicle.getAccessories());
            foundVehicle.get().setUsed(vehicle.getUsed());
            foundVehicle.get().setVehicleStatus(vehicle.getVehicleStatus());
            foundVehicle.get().setVehicleType(vehicle.getVehicleType());
            return vehicleRepository.saveAndFlush(foundVehicle.get());
        } else {
            return null;
        }
    }

    public Boolean deleteVehicle(long id) {
        vehicleRepository.deleteById(id);
        return !vehicleRepository.existsById(id);
    }

    public Vehicle updateVehicleStatusFromId(long id, String status) {
        if(!VehicleStatus.isValidVehicleStatus(status.toUpperCase())){
            return null;
        }

        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        if (vehicle.isPresent()) {
            vehicle.get().setVehicleStatus(VehicleStatus.valueOf(status.toUpperCase()));
            return vehicleRepository.saveAndFlush(vehicle.get());
        } else {
            return null;
        }
    }

    public List<Vehicle> findByStatusAndUsed(String status, Boolean used) {
        String statusString = status.toUpperCase();
        VehicleStatus s = VehicleStatus.valueOf(statusString);
        return new ArrayList<>(vehicleRepository.findByVehicleStatusAndUsed(s, used));
    }

    public Vehicle getDetailsOfVehicle(long id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        if (vehicle.isPresent()) {
            return vehicle.get();
        } else {
            return null;
        }
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }


    public void deleteSingleVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

}
