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

    public Vehicle createVehicle(Vehicle vehicle){
        return vehicleRepository.saveAndFlush(vehicle);
    }
    public Vehicle updateVehicle(long id,Vehicle vehicle){
        if(vehicleRepository.existsById(id)) {
            vehicle.setId(id);
            return vehicleRepository.saveAndFlush(vehicle);
        }else {
            return null;
        }
    }
    public Boolean deleteVehicle(long id){
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
        return new ArrayList<>(vehicleRepository.findByVehicleStatusAndUsed(s,used));
    }
    public Vehicle getDetailsOfVehicle(long id) {
        if (vehicleRepository.existsById(id)) {
            return vehicleRepository.findById(id).get();
        } else {
            return null;
        }
    }
    public List<Vehicle> getAllVehicles(){
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
        updatedVehicle.setStatus(vehicle.getStatus());
        return vehicleRepository.saveAndFlush(updatedVehicle);
    }

    public void deleteSingleVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

}
