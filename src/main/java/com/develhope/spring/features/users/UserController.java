package com.develhope.spring.features.users;

import com.develhope.spring.exception.NotFoundException;
import com.develhope.spring.features.users.dto.CreateUserRequest;
import com.develhope.spring.features.users.dto.UserResponse;
import com.develhope.spring.features.vehicle.VehicleEntity;
import com.develhope.spring.features.vehicle.VehicleService;
import jakarta.websocket.server.PathParam;
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
    private final UserMapper userMapper;

    @PostMapping(path = USER_PATH)
    public ResponseEntity<?> createOne(@RequestBody CreateUserRequest userRequest) {
        if(userRequest.getUserType() == UserType.ADMIN){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        
        UserModel userModel = userMapper.convertUserRequestToModel(userRequest);
        UserResponse userResponse = userService.createUser(userModel);
        if(userResponse != null){
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = USER_PATH)
    public List<UserEntity> getall() {
        return userService.getAllUsers();
    }

    @GetMapping(path = USER_PATH_ID)
    public ResponseEntity getone(@PathVariable Long userId) {
        UserResponse updatedUserEntity = userService.getSingleUser(userId);
        if (updatedUserEntity == null) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(updatedUserEntity, HttpStatus.OK);
    }

    @PutMapping(path = USER_PATH_ID)
    public ResponseEntity<?> updateOne(@PathVariable Long userId, @RequestBody CreateUserRequest userRequest) {
        UserModel userModel = userMapper.convertUserRequestToModel(userRequest);
        UserResponse updatedUserEntity = userService.updateUser(userId, userModel);
        if (updatedUserEntity == null) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(updatedUserEntity, HttpStatus.OK);
    }

    @PatchMapping(path = USER_PATH_ID)
    public ResponseEntity<?> patchOne(@PathVariable Long userId, @RequestBody CreateUserRequest userRequest) {
        UserModel userModel = userMapper.convertUserRequestToModel(userRequest);
        UserResponse updatedUserEntity = userService.patchUser(userId, userModel);
        if (updatedUserEntity == null) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(updatedUserEntity, HttpStatus.OK);
    }

    @PatchMapping(path = USER_PATH_ID)
    public ResponseEntity<?> updatePassword(@PathVariable Long userId, @RequestParam(required = true) String password, @RequestParam(required = true) Long requester_id) {
        UserResponse updatedUserEntity = userService.updatePassword(userId, password, requester_id);
        if (updatedUserEntity == null) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(updatedUserEntity, HttpStatus.OK);
    }

    @PatchMapping(path = USER_PATH_ID)
    public ResponseEntity<?> updateUsername(@PathVariable Long userId, @RequestParam(required = true) String username, @RequestParam(required = true) Long requester_id) {
        UserResponse updatedUserEntity = userService.updateUserName(userId, username, requester_id);
        if (updatedUserEntity == null) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(updatedUserEntity, HttpStatus.OK);
    }

    @PatchMapping(path = USER_PATH_ID)
    public ResponseEntity<?> updateTelephoneNumber(@PathVariable Long userId, @RequestParam(required = true) String phone, @RequestParam(required = true) Long requester_id) {
        UserResponse updatedUserEntity = userService.updateTelephoneNumber(userId, phone, requester_id);
        if (updatedUserEntity == null) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(updatedUserEntity, HttpStatus.OK);
    }

    @PatchMapping(path = USER_PATH_ID)
    public ResponseEntity<?> updateEmail(@PathVariable Long userId, @RequestParam(required = true) String email, @RequestParam(required = true) Long requester_id) {
        UserResponse updatedUserEntity = userService.updateEmail(userId, email, requester_id);
        if (updatedUserEntity == null) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(updatedUserEntity, HttpStatus.OK);
    }

    @DeleteMapping(path = USER_PATH_ID)
    public ResponseEntity deleteOne(@PathVariable Long userId, @RequestParam(required = true) Long requester_id) {
        if (userService.deleteSingleUser(userId, requester_id)) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
