package com.develhope.spring.controllers;

import com.develhope.spring.entities.vehicle.Vehicle;
import com.develhope.spring.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/vehicle/create")
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return vehicleService.createVehicle(vehicle);
    }

    @PutMapping("/vehicle/update/{id}")
    public Vehicle updateVehicle(@PathVariable long id, @RequestBody Vehicle vehicle) {
        return vehicleService.updateVehicle(id, vehicle);
    }

    @DeleteMapping("/vehicle/delete/{id}")
    public Boolean deleteVehicle(@PathVariable long id) {
        return vehicleService.deleteVehicle(id);
    }

    @PatchMapping("/vehicle/status/update/{id}")
    public Vehicle updateVehicleStatusFromId(@PathVariable long id, @RequestParam String status) {
        return vehicleService.updateVehicleStatusFromId(id, status);
    }

    @GetMapping("/vehicle/get/by/status/used")
    public List<Vehicle> findByStatusAndUsed(@RequestParam String status, @RequestParam Boolean used) {
        return vehicleService.findByStatusAndUsed(status, used);
    }
    @GetMapping("/vehicles/get")
    public List<Vehicle> getAllVehicles(){
        return vehicleService.getAllVehicles();
    }
}
