package com.develhope.spring.features.vehicle;

import com.develhope.spring.features.vehicle.dto.CreateVehicleRequest;
import com.develhope.spring.features.vehicle.dto.VehicleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/create")
    public VehicleResponse createVehicle(@RequestBody CreateVehicleRequest createVehicleRequest) {
        return vehicleService.createVehicle(createVehicleRequest);
    }

    @PutMapping("/update/{id}")
    public VehicleResponse updateVehicle(@PathVariable Long id, @RequestBody CreateVehicleRequest createVehicleRequest) {
        return vehicleService.updateVehicle(id, createVehicleRequest);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteVehicle(@PathVariable Long id) {
        return vehicleService.deleteVehicle(id);
    }

    @PatchMapping("/status/{id}")
    public VehicleEntity updateVehicleStatusFromId(@PathVariable Long id, @RequestParam String status) {
        return vehicleService.updateVehicleStatusFromId(id, status);
    }

    @GetMapping("/bystatus")
    public ResponseEntity<?> findByStatusAndUsed(@RequestParam String status, @RequestParam Boolean used) {
        return vehicleService.findByStatusAndUsed(status, used);
    }

    @GetMapping("/all")
    public List<VehicleEntity> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }
}
