package com.develhope.spring.controllers;

import com.develhope.spring.entities.vehicle.Vehicle;
import com.develhope.spring.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/create")
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return vehicleService.createVehicle(vehicle);
    }

    @PutMapping("/update/{id}")
    public Vehicle updateVehicle(@PathVariable long id, @RequestBody Vehicle vehicle) {
        return vehicleService.updateVehicle(id, vehicle);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteVehicle(@PathVariable long id) {
        return vehicleService.deleteVehicle(id);
    }

    @PatchMapping("/status/{id}")
    public Vehicle updateVehicleStatusFromId(@PathVariable long id, @RequestParam String status) {
        return vehicleService.updateVehicleStatusFromId(id, status);
    }

    @GetMapping("/bystatus/used")
    public List<Vehicle> findByStatusAndUsed(@RequestParam String status, @RequestParam Boolean used) {
        return vehicleService.findByStatusAndUsed(status, used);
    }

    @GetMapping("/all")
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }
}
