package com.develhope.spring.services;

import com.develhope.spring.entities.vehicle.Vehicle;
import com.develhope.spring.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    @Autowired
    VehicleRepository vehicleRepository;

    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.saveAndFlush(vehicle);
    }

    public Vehicle getSingleVehicle(Long id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        return vehicle.orElse(null);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle updateVehicle(Long id, Vehicle vehicle) {
        Vehicle updatedVehicle = getSingleVehicle(id);
        updatedVehicle.setModel(vehicle.getModel());
        updatedVehicle.setBrand(vehicle.getBrand());
        updatedVehicle.setDisplacement(vehicle.getDisplacement());
        updatedVehicle.setColor(vehicle.getColor());
        updatedVehicle.setPower(vehicle.getPower());
        updatedVehicle.setShift(vehicle.getShift());
        updatedVehicle.setYearOfmatriculation(vehicle.getYearOfmatriculation());
        updatedVehicle.setFuelType(vehicle.getFuelType());
        updatedVehicle.setPrice(vehicle.getPrice());
        updatedVehicle.setDiscount(vehicle.getDiscount());
        updatedVehicle.setAccesories(vehicle.getAccesories());
        updatedVehicle.setVehicleStatus(vehicle.getVehicleStatus());
        return vehicleRepository.saveAndFlush(updatedVehicle);
    }

    public void deleteSingle(Long id) {
        vehicleRepository.deleteById(id);
    }
}
