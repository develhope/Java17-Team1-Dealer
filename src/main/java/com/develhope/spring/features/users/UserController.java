package com.develhope.spring.features.users;

import com.develhope.spring.exception.NotFoundException;
import com.develhope.spring.features.users.dto.CreateUserRequest;
import com.develhope.spring.features.users.dto.PatchUserRequest;
import com.develhope.spring.features.users.dto.UserResponse;
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


    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    @PostMapping(path = USER_PATH)
    public ResponseEntity<?> createOne(@RequestBody CreateUserRequest userRequest) {
        /*if(userRequest.getUserType() == UserType.ADMIN){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }*/

        UserResponse userResponse = userService.createUser(userRequest);
        if (userResponse == null) {
            return new ResponseEntity<>(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping(path = USER_PATH)
    public List<UserResponse> getall() {
        return userService.getAllUsers();
    }

    @GetMapping(path = USER_PATH_ID)
    public ResponseEntity<?> getone(@PathVariable Long userId) {
        UserResponse userResponse = userService.getUser(userId);
        if (userResponse == null) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PatchMapping(path = USER_PATH_ID)
    public ResponseEntity<?> patchUser(@PathVariable Long userId, @RequestBody PatchUserRequest patchUserRequest, @RequestParam(required = true) Long requester_id) {
        UserResponse updatedUserEntity = userService.patchUser(userId, patchUserRequest, requester_id);
        if (updatedUserEntity == null) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(updatedUserEntity, HttpStatus.OK);
    }

    @PatchMapping(path = USER_PATH_ID + "/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long userId, @RequestParam(required = true) String password, @RequestParam(required = true) Long requester_id) {
        UserResponse updatedUserEntity = userService.updatePassword(userId, password, requester_id);
        if (updatedUserEntity == null) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(updatedUserEntity, HttpStatus.OK);
    }

    @PatchMapping(path = USER_PATH_ID + "/username")
    public ResponseEntity<?> updateUsername(@PathVariable Long userId, @RequestParam(required = true) String username, @RequestParam(required = true) Long requester_id) {
        UserResponse updatedUserEntity = userService.updateUserName(userId, username, requester_id);
        if (updatedUserEntity == null) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(updatedUserEntity, HttpStatus.OK);
    }

    @PatchMapping(path = USER_PATH_ID + "/phone")
    public ResponseEntity<?> updateTelephoneNumber(@PathVariable Long userId, @RequestParam(required = true) String phone, @RequestParam(required = true) Long requester_id) {
        UserResponse updatedUserEntity = userService.updateTelephoneNumber(userId, phone, requester_id);
        if (updatedUserEntity == null) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(updatedUserEntity, HttpStatus.OK);
    }

    @PatchMapping(path = USER_PATH_ID + "/email")
    public ResponseEntity<?> updateEmail(@PathVariable Long userId, @RequestParam(required = true) String email, @RequestParam(required = true) Long requester_id) {
        UserResponse updatedUserEntity = userService.updateEmail(userId, email, requester_id);
        if (updatedUserEntity == null) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(updatedUserEntity, HttpStatus.OK);
    }

    @DeleteMapping(path = USER_PATH_ID)
    public ResponseEntity<?> deleteOne(@PathVariable Long userId, @RequestParam(required = true) Long requester_id) {
        if (userService.deleteSingleUser(userId, requester_id)) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    //ADMIN ROUTES
    @GetMapping(path = USER_PATH_ID + "/salescount")
    public ResponseEntity<?> getSalesCountBySellerId(@PathVariable Long sellerId) {
        return userService.getSalesCountBySellerId(sellerId);
    }

    @GetMapping(path = USER_PATH_ID + "/profit")
    public ResponseEntity<?> getSalesTotalPriceBySellerId(@PathVariable Long sellerId) {
        return userService.getSalesTotalPriceBySellerId(sellerId);
    }


}
