package com.develhope.spring.services;

import com.develhope.spring.entities.users.User;
import com.develhope.spring.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    public Optional<User> getSingleUser(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User user) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            foundUser.get().setPassword(user.getPassword());
            foundUser.get().setName(user.getName());
            foundUser.get().setSurname(user.getSurname());
            foundUser.get().setEmail(user.getEmail());
            foundUser.get().setTelephoneNumber(user.getTelephoneNumber());
            foundUser.get().setUserType(user.getUserType());
            return userRepository.saveAndFlush(foundUser.get());
        } else {
            return null;
        }
    }

    public Boolean deleteSingleUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;

    }
}
