package com.develhope.spring.features.users;

import com.develhope.spring.exception.NotFoundException;
import com.develhope.spring.features.vehicle.VehicleEntity;
import com.develhope.spring.features.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    public static final String USER_PATH = "/user";
    public static final String USER_PATH_ID = USER_PATH + "/{userId}";

    private final UserService userService;
    private final VehicleService vehicleService;

    @PostMapping(path = USER_PATH)
    public ResponseEntity<?> createOne(@RequestBody UserEntity userEntity) {
        UserEntity savedUserEntity = userService.createUser(userEntity);
        if (savedUserEntity.getId() == null) {
            return new ResponseEntity<>(savedUserEntity, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(savedUserEntity, HttpStatus.OK);
    }

    @GetMapping(path = USER_PATH)
    public List<UserEntity> getall() {
        return userService.getAllUsers();
    }

    @GetMapping(path = USER_PATH_ID)
    public UserEntity getone(@PathVariable Long userId) {
        return userService.getSingleUser(userId).orElseThrow(NotFoundException::new);
    }

    @PutMapping(path = USER_PATH_ID)
    public ResponseEntity<?> updateOne(@PathVariable Long userId, @RequestBody UserEntity userEntity) {
        UserEntity updatedUserEntity = userService.updateUser(userId, userEntity);
        if (updatedUserEntity == null) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(updatedUserEntity, HttpStatus.OK);
    }

    @DeleteMapping(path = USER_PATH_ID)
    public ResponseEntity deleteOne(@PathVariable Long userId) {
        if (userService.deleteSingleUser(userId)) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = USER_PATH + "/vehicle/all")
    public List<VehicleEntity> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }
}
