package com.develhope.spring.controllers;

import com.develhope.spring.entities.vehicle.Vehicle;
import com.develhope.spring.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/vehicle")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    @PostMapping(path = "")
    public Vehicle createOne(@RequestBody Vehicle vehicle) {
        return vehicleService.createVehicle(vehicle);
    }

    @GetMapping(path = "/all")
    public List<Vehicle> getall() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping(path = "/{id}")
    public Vehicle getone(@PathVariable Long id) {
        return vehicleService.getSingleVehicle(id);
    }

    @PutMapping(path = "/{id}")
    public Vehicle updateOne(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        return vehicleService.updateVehicle(id, vehicle);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteOne(@PathVariable Long id) {
        vehicleService.deleteSingle(id);
    }

}
