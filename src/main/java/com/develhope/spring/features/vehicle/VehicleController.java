package com.develhope.spring.features.vehicle;

import com.develhope.spring.features.users.Role;
import com.develhope.spring.features.users.UserEntity;
import com.develhope.spring.features.vehicle.dto.CreateVehicleRequest;
import com.develhope.spring.features.vehicle.dto.MostSoldOrOrderedVehiclePeriodRequest;
import com.develhope.spring.features.vehicle.dto.PatchVehicleRequest;
import com.develhope.spring.features.vehicle.dto.VehicleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VehicleController {
    public static final String VEHICLE_PATH = "/vehicle";
    public static final String VEHICLE_PATH_ID = VEHICLE_PATH + "/{vehicleId}";

    private final VehicleService vehicleService;

    @PostMapping(path = VEHICLE_PATH)
    public ResponseEntity<?> createVehicle(@AuthenticationPrincipal UserEntity user,
                                           @RequestBody CreateVehicleRequest createVehicleRequest) {
        return vehicleService.createVehicle(user, createVehicleRequest);
    }

    @PatchMapping(path = VEHICLE_PATH_ID)
    public ResponseEntity<?> patchVehicle(@AuthenticationPrincipal UserEntity user,
                                          @PathVariable Long vehicleId,
                                          @RequestBody PatchVehicleRequest patchVehicleRequest) {
        return vehicleService.patchVehicle(user, vehicleId, patchVehicleRequest);
    }

    @DeleteMapping(path = VEHICLE_PATH_ID)
    public Boolean deleteVehicle(@AuthenticationPrincipal UserEntity user,
                                 @PathVariable Long vehicleId) {
        return vehicleService.deleteVehicle(user, vehicleId);
    }

    @PatchMapping(path = VEHICLE_PATH_ID + "/status")
    public ResponseEntity<?> patchStatus(@AuthenticationPrincipal UserEntity user,
                                         @PathVariable Long vehicleId,
                                         @RequestParam String status) {
        return vehicleService.patchStatus(user, vehicleId, status);
    }

    @GetMapping(path = VEHICLE_PATH + "/bystatus")
    public ResponseEntity<?> findByStatus(@AuthenticationPrincipal UserEntity user,
                                          @RequestParam String status) {
        return vehicleService.findByStatus(user, status);
    }


    @GetMapping(path = VEHICLE_PATH_ID)
    public ResponseEntity<?> getSingleVehicle(@PathVariable Long vehicleId) {
        return vehicleService.getSingleVehicle(vehicleId);
    }

    @GetMapping(path = VEHICLE_PATH + "/all")
    public List<VehicleResponse> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    //ADMIN ROUTES
    @GetMapping(path = VEHICLE_PATH + "/mostsold")
    public ResponseEntity<?> getMostSoldVehiclePeriod(@AuthenticationPrincipal UserEntity user,
                                                      @RequestBody MostSoldOrOrderedVehiclePeriodRequest request) {
        if (user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return vehicleService.getMostSoldVehiclePeriod(request.getStartDate(), request.getEndDate());
    }

    @GetMapping(path = VEHICLE_PATH + "/mostorderedinaperiod")
    public ResponseEntity<?> getMostOrderedVehiclePeriod(@AuthenticationPrincipal UserEntity user,
                                                         @RequestBody MostSoldOrOrderedVehiclePeriodRequest request) {
        if (user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return vehicleService.getMostOrderedVehiclePeriod(request.getStartDate(), request.getEndDate());
    }

    @GetMapping(path = VEHICLE_PATH + "/highestpricesold")
    public ResponseEntity<?> getHighestPriceSold(@AuthenticationPrincipal UserEntity user) {
        if (user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return vehicleService.getHighestPriceSold();
    }

    @GetMapping(path = VEHICLE_PATH + "/lowestpricesold")
    public ResponseEntity<?> getLowestPriceSold(@AuthenticationPrincipal UserEntity user) {
        if (user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return vehicleService.getLowestPriceSold();
    }
}
