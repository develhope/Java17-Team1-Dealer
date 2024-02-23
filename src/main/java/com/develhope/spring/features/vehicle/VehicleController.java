package com.develhope.spring.features.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/create")
    public VehicleEntity createVehicle(@RequestBody VehicleEntity vehicleEntity) {
        return vehicleService.createVehicle(vehicleEntity);
    }

    @PutMapping("/update/{id}")
    public VehicleEntity updateVehicle(@PathVariable long id, @RequestBody VehicleEntity vehicleEntity) {
        return vehicleService.updateVehicle(id, vehicleEntity);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteVehicle(@PathVariable long id) {
        return vehicleService.deleteVehicle(id);
    }

    @PatchMapping("/status/{id}")
    public VehicleEntity updateVehicleStatusFromId(@PathVariable long id, @RequestParam String status) {
        return vehicleService.updateVehicleStatusFromId(id, status);
    }

    @GetMapping("/bystatus/used")
    public List<VehicleEntity> findByStatusAndUsed(@RequestParam String status, @RequestParam Boolean used) {
        return vehicleService.findByStatusAndUsed(status, used);
    }

    @GetMapping("/all")
    public List<VehicleEntity> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }
}
