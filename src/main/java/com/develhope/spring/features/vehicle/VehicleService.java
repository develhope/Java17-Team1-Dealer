package com.develhope.spring.features.vehicle;

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
    public Vehicle getSingleVehicle(Long id) {
        Optional<Vehicle> user = vehicleRepository.findById(id);
        return user.orElse(null);
    }


    public Vehicle updateVehicle(long id, Vehicle vehicle) {
      Vehicle vehicleToUpdate = getSingleVehicle(id);
      vehicleToUpdate.setModel(vehicle.getModel());
      vehicleToUpdate.setBrand(vehicle.getBrand());
      vehicleToUpdate.setDisplacement(vehicle.getDisplacement());
      vehicleToUpdate.setColor(vehicle.getColor());
      vehicleToUpdate.setPower(vehicle.getPower());
      vehicleToUpdate.setShift(vehicle.getShift());
      vehicleToUpdate.setYearOfmatriculation(vehicle.getYearOfmatriculation());
      vehicleToUpdate.setFuelType(vehicle.getFuelType());
      vehicleToUpdate.setPrice(vehicle.getPrice());
      vehicleToUpdate.setDiscount(vehicle.getDiscount());
      vehicleToUpdate.setAccesories(vehicle.getAccesories());
      vehicleToUpdate.setUsed(vehicle.getUsed());
      vehicleToUpdate.setVehicleStatus(vehicle.getVehicleStatus());
      vehicleToUpdate.setVehicleType(vehicle.getVehicleType());
      vehicleRepository.saveAndFlush(vehicleToUpdate);
      return vehicleToUpdate;
    }

    public Boolean deleteVehicle(long id) {
        vehicleRepository.deleteById(id);
        return !vehicleRepository.existsById(id);
    }

    public Vehicle updateVehicleStatusFromId(long id, String status) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        if (vehicle.isPresent()) {
            String statusString = status.toUpperCase();
            VehicleStatus s = VehicleStatus.valueOf(statusString);
            vehicle.get().setVehicleStatus(s);
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
        if (vehicleRepository.existsById(id)) {
            return vehicleRepository.findById(id).get();
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
