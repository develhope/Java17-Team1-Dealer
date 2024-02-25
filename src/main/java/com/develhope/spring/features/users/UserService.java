package com.develhope.spring.features.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
            foundUser.get().setName(userEntity.getName());
            foundUser.get().setSurname(userEntity.getSurname());
            foundUser.get().setEmail(userEntity.getEmail());
            foundUser.get().setTelephoneNumber(userEntity.getTelephoneNumber());
            foundUser.get().setPassword(userEntity.getPassword()); //here should already be a hashed string
            return userRepository.saveAndFlush(userEntity);
        } else {
            return null;
        }
    }

    //TO DO: general functions for better checks (like, userNameCheck(string) -> bool)
    //right now these are simple, just checking empty/null
    public UserEntity patchUser(Long id, UserEntity userEntity) {
        Optional<UserEntity> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            if(StringUtils.hasText(userEntity.getEmail())){
                foundUser.get().setEmail(userEntity.getEmail());
            }

            if(StringUtils.hasText(userEntity.getName())){
                foundUser.get().setName(userEntity.getName());
            }

            if(StringUtils.hasText(userEntity.getSurname())){
                foundUser.get().setSurname(userEntity.getSurname());
            }

            if(StringUtils.hasText(userEntity.getTelephoneNumber())){
                foundUser.get().setTelephoneNumber(userEntity.getTelephoneNumber());
            }

            if(StringUtils.hasText(userEntity.getPassword())){
                foundUser.get().setPassword(userEntity.getPassword());
            }
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
