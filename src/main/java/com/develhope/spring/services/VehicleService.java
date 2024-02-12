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

    public Optional<Vehicle> getVehicle(Long id) {
        return vehicleRepository.findById(id);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle updateVehicle(Long id, Vehicle vehicle) {
        return vehicleRepository.saveAndFlush(vehicle);
    }

    public void deleteSingle(Long id) {
        vehicleRepository.deleteById(id);
    }
}
