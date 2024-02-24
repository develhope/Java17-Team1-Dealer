package com.develhope.spring.features.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity createUser(UserEntity userEntity) {
        return userRepository.saveAndFlush(userEntity);
    }

    public Optional<UserEntity> getSingleUser(Long id) {
        return userRepository.findById(id);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity updateUser(Long id, UserEntity userEntity) {
        Optional<UserEntity> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {

            userEntity.setId(foundUser.get().getId());
            return userRepository.saveAndFlush(userEntity);
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
