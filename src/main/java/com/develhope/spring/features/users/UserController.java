package com.develhope.spring.features.users;

import com.develhope.spring.exception.NotFoundException;
import com.develhope.spring.features.users.dto.CreateUserRequest;
import com.develhope.spring.features.users.dto.PatchUserRequest;
import com.develhope.spring.features.users.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    public static final String USER_PATH = "/user";
    public static final String USER_PATH_ID = USER_PATH + "/{userId}";


    private final UserServiceImpl userService;

    @PostMapping(path = USER_PATH)
    public ResponseEntity<?> createOne(@AuthenticationPrincipal UserEntity user, @RequestBody CreateUserRequest userRequest) {
        if (user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        return userService.createUser(userRequest);
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
    public ResponseEntity<?> patchUser(@AuthenticationPrincipal UserEntity user,
                                       @PathVariable Long userId,
                                       @RequestBody PatchUserRequest patchUserRequest) {
        return userService.patchUser(user, userId, patchUserRequest);
    }

    @PatchMapping(path = USER_PATH_ID + "/password")
    public ResponseEntity<?> updatePassword(@AuthenticationPrincipal UserEntity user,
                                            @PathVariable Long userId,
                                            @RequestParam(required = true) String password) {
        return userService.updatePassword(user, userId, password);
    }

    @PatchMapping(path = USER_PATH_ID + "/username")
    public ResponseEntity<?> updateUsername(@AuthenticationPrincipal UserEntity user,
                                            @PathVariable Long userId,
                                            @RequestParam(required = true) String username) {
        return userService.updateUserName(user, userId, username);
    }

    @PatchMapping(path = USER_PATH_ID + "/phone")
    public ResponseEntity<?> updateTelephoneNumber(@AuthenticationPrincipal UserEntity user,
                                                   @PathVariable Long userId,
                                                   @RequestParam(required = true) String phone) {
        return userService.updateTelephoneNumber(user, userId, phone);
    }

    @PatchMapping(path = USER_PATH_ID + "/email")
    public ResponseEntity<?> updateEmail(@AuthenticationPrincipal UserEntity user,
                                         @PathVariable Long userId, @RequestParam(required = true) String email) {
        return userService.updateEmail(user, userId, email);
    }

    @DeleteMapping(path = USER_PATH_ID)
    public ResponseEntity<?> deleteOne(@AuthenticationPrincipal UserEntity user,
                                       @PathVariable Long userId) {
        userService.deleteSingleUser(user, userId);
        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }


    //ADMIN ROUTES
    @GetMapping(path = USER_PATH_ID + "/salescount")
    public ResponseEntity<?> getSalesCountBySellerId(@AuthenticationPrincipal UserEntity user, @PathVariable Long sellerId) {
        if (user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return userService.getSalesCountBySellerId(sellerId);
    }

    @GetMapping(path = USER_PATH_ID + "/profit")
    public ResponseEntity<?> getSalesTotalPriceBySellerId(@AuthenticationPrincipal UserEntity user, @PathVariable Long sellerId) {
        if (user.getRole() != Role.ADMIN) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return userService.getSalesTotalPriceBySellerId(sellerId);
    }


}
