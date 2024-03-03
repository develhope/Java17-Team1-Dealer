package com.develhope.spring.features.vehicle;

import com.develhope.spring.features.vehicle.dto.CreateVehicleRequest;
import com.develhope.spring.features.vehicle.dto.PatchVehicleRequest;
import com.develhope.spring.features.vehicle.dto.VehicleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class VehicleController {
    public static final String VEHICLE_PATH = "/vehicle";
    public static final String VEHICLE_PATH_ID = VEHICLE_PATH + "/{vehicleId}";

    private final VehicleService vehicleService;

    @PostMapping(path = VEHICLE_PATH)
    public ResponseEntity<?> createVehicle(@RequestBody CreateVehicleRequest createVehicleRequest, @RequestParam(required = true) Long requester_id) {
        VehicleResponse vehicleResponse = vehicleService.createVehicle(createVehicleRequest, requester_id);
        if (vehicleResponse == null) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(vehicleResponse, HttpStatus.OK);
    }

    @PatchMapping(path = VEHICLE_PATH_ID)
    public VehicleResponse patchVehicle(@PathVariable Long vehicleId, @RequestBody PatchVehicleRequest patchVehicleRequest, @RequestParam(required = true) Long requester_id) {
        return vehicleService.patchVehicle(vehicleId, patchVehicleRequest, requester_id);
    }

    @DeleteMapping(path = VEHICLE_PATH_ID)
    public Boolean deleteVehicle(@PathVariable Long vehicleId, @RequestParam(required = true) Long requester_id) {
        return vehicleService.deleteVehicle(vehicleId, requester_id);
    }

    @PatchMapping(path = VEHICLE_PATH_ID + "/status")
    public VehicleResponse patchStatus(@PathVariable Long vehicleId, @RequestParam String status, @RequestParam(required = true) Long requester_id) {
        return vehicleService.patchStatus(vehicleId, status, requester_id);
    }

    @GetMapping(path = VEHICLE_PATH + "/bystatus")
    public ResponseEntity<?> findByStatus(@RequestParam String status, @RequestParam(required = true) Long requester_id) {
        return vehicleService.findByStatus(status, requester_id);
    }


    @GetMapping(path = VEHICLE_PATH_ID)
    public ResponseEntity<?> getSingleVehicle(@PathVariable Long vehicleId) {
        VehicleResponse vehicleResponse = vehicleService.getSingleVehicle(vehicleId);
        if (vehicleResponse == null) {
            return new ResponseEntity<>(vehicleResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(vehicleResponse, HttpStatus.OK);
    }

    @GetMapping(path = VEHICLE_PATH + "/all")
    public List<VehicleResponse> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }
}
