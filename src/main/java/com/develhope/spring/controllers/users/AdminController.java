package com.develhope.spring.controllers.users;

import com.develhope.spring.entities.operations.Order;
import com.develhope.spring.entities.vehicle.Vehicle;
import com.develhope.spring.services.users.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/vehicle/create")
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return adminService.createVehicle(vehicle);
    }

    @PutMapping("/vehicle/update/{id}")
    public Vehicle updateVehicle(@PathVariable long id, @RequestBody Vehicle vehicle) {
        return adminService.updateVehicle(id, vehicle);
    }

    @DeleteMapping("vehicle/delete/{id}")
    public Boolean deleteVehicle(@PathVariable long id) {
        return adminService.deleteVehicle(id);
    }

    @PatchMapping("vehicle/status/update/{id}")
    public Vehicle updateVehicleStatusFromId(@PathVariable long id, @RequestParam String status) {
        return adminService.updateVehicleStatusFromId(id, status);
    }

    @GetMapping("vehicle/get/by/status/used")
    public List<Vehicle> findByStatusAndUsed(@RequestParam String status, @RequestParam Boolean used) {
        return adminService.findByStatusAndUsed(status, used);
    }
}
