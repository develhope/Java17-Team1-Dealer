package com.develhope.spring.controllers;

import com.develhope.spring.entities.users.User;
import com.develhope.spring.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    public static final String USER_PATH = "/user";
    public static final String USER_PATH_ID = "/user/{id}";

    private final UserService userService;

    @PostMapping(path = USER_PATH)
    public ResponseEntity<?> createOne(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        if (savedUser.getId() == null) {
            return new ResponseEntity<>(savedUser, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    @GetMapping(path = USER_PATH)
    public List<User> getall() {
        return userService.getAllUsers();
    }

    @GetMapping(path = USER_PATH_ID)
    public User getone(@PathVariable Long id) {
        return userService.getSingleUser(id).orElseThrow(NotFoundException::new);
    }

    @PutMapping(path = USER_PATH_ID)
    public ResponseEntity<?> updateOne(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        if(updatedUser == null) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping(path = USER_PATH_ID)
    public ResponseEntity deleteOne(@PathVariable Long id) {
        if (userService.deleteSingleUser(id)) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
