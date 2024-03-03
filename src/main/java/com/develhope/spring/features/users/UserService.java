package com.develhope.spring.features.users;

import com.develhope.spring.features.rentals.dto.RentalResponse;
import com.develhope.spring.features.users.dto.CreateUserRequest;
import com.develhope.spring.features.users.dto.PatchUserRequest;
import com.develhope.spring.features.users.dto.UserResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    private Boolean isRequesterIDValid(Optional<UserEntity> requesterUser, Long id) {
        if (requesterUser.isEmpty()) {
            return false;
        }

        if (requesterUser.get().getId() != id && requesterUser.get().getUserType() != UserType.ADMIN) {
            return false;
        }
        return true;
    }

    public Boolean isRequesterIDValid(UserEntity originalUser, Long requester_id) {
        if (originalUser == null) {
            return false;
        }

        Optional<UserEntity> userRequester = userRepository.findById(requester_id);

        if (userRequester.isEmpty()) {
            return false;
        }

        if (originalUser.getId() != requester_id && userRequester.get().getUserType() != UserType.ADMIN) {
            return false;
        }
        return true;
    }

    private UserResponse generateUserResponseFromEntity(UserEntity userEntity) {
        return userMapper.convertUserEntityToResponse(userEntity);
    }

    public UserResponse createUser(CreateUserRequest userRequest) {
        
        /*
        if (!StringUtils.hasText(userRequest.getName())) {
            return null; //empty
        }

        if (!StringUtils.hasText(userRequest.getEmail())) {
            return null; //empty
        }

        if (!StringUtils.hasText(userRequest.getPassword())) {
            return null; //empty
        }

        if (userRequest.getName().length() < 3) {
            return null; //too short
        }

        if (userRequest.getName().length() > 20) {
            return null; //too long
        }

        if (userRequest.getEmail().length() < 5) {
            return null; //too short
        }

        if (userRequest.getEmail().length() > 50) {
            return null; //too long
        }

        if (userRequest.getTelephoneNumber().length() < 5) {
            return null; //too short
        }

        if (userRequest.getTelephoneNumber().length() > 11) {
            return null; //too long
        }

        if (userRequest.getPassword().length() < 5) {
            return null; //too short
        }

        if (userRequest.getPassword().length() > 20) {
            return null; //too long
        }

         if (userRepository.existsByEmail(userRequest.getEmail())) {
            return null; //email exists
        }

        if (userRepository.existsByTelephoneNumber(userRequest.getTelephoneNumber())) {
            return null; //telephone exists
        }

        if (userRepository.existsBySurmame(userRequest.getSurname())) {
            return null; //surname exists
        }

        userRequest.setUserType(userRequest.getUserType().toUpperCase());
        if (!UserType.isValidUserType(userRequest.getUserType())) {
            return null; //invalid user type
        }
        */
        UserEntity userEntity = userMapper.convertUserRequestToEntity(userRequest);
        return userMapper.convertUserEntityToResponse(userRepository.saveAndFlush(userEntity));
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    //TO DO: general functions for better checks (like, userNameCheck(string) -> bool)
    //right now these are simple, just checking empty/null
    public UserResponse patchUser(Long user_id, PatchUserRequest patchUserRequest, Long requester_id) {
        AtomicReference<Optional<UserResponse>> atomicReference = new AtomicReference<>();


        userRepository.findById(user_id).ifPresentOrElse(userEntity -> {
            if (!isRequesterIDValid(userEntity, requester_id)) {
                atomicReference.set(Optional.empty());
                return;
            }

            if (StringUtils.hasText(patchUserRequest.getName())) {
                userEntity.setName(patchUserRequest.getName());
            }

            if (StringUtils.hasText(patchUserRequest.getEmail())) {
                userEntity.setEmail(patchUserRequest.getEmail());
            }

            if (StringUtils.hasText(patchUserRequest.getTelephoneNumber())) {
                userEntity.setTelephoneNumber(patchUserRequest.getTelephoneNumber());
            }

            if (StringUtils.hasText(patchUserRequest.getPassword())) {
                userEntity.setPassword(patchUserRequest.getPassword());
            }

            atomicReference.set(Optional.of(userMapper.convertUserEntityToResponse(userRepository.saveAndFlush(userEntity))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });


        if (atomicReference.get().isEmpty()) {
            return null;
        }
        return atomicReference.get().get();

    }

    private Optional<UserEntity> checkAndGetkUser(Long searched_id, Long requester_id) { //TODO: put this check before
        AtomicReference<Optional<UserEntity>> atomicReference = new AtomicReference<>();
        userRepository.findById(requester_id).ifPresentOrElse(userEntity -> {
            atomicReference.set(Optional.of(userEntity));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        if (!isRequesterIDValid(atomicReference.get(), searched_id)) {
            return Optional.empty();
        }

        return requester_id == searched_id ? atomicReference.get() : userRepository.findById(searched_id);
    }

    public UserResponse getUser(Long id, Long requester_id) {
        Optional<UserEntity> foundUser = checkAndGetkUser(id, requester_id);
        return foundUser.map(this::generateUserResponseFromEntity).orElse(null);
    }

    //TODO: proper returns for proper errors;
    public UserResponse updatePassword(Long id, String password, Long requester_id) { //already hashed
        Optional<UserEntity> foundUser = checkAndGetkUser(id, requester_id);

        if (foundUser.isPresent()) {
            if (password.length() < 5) {
                return null; //too short
            }

            if (password.length() > 20) {
                return null; //too long
            }

            if (!StringUtils.hasText(password)) {
                return null; //empty
            }

            foundUser.get().setPassword(password);
            var savedUser = userRepository.saveAndFlush(foundUser.get());
            return generateUserResponseFromEntity(savedUser);
        } else {
            return null;
        }
    }

    public UserResponse updateUserName(Long id, String userName, Long requester_id) {
        if (userName.length() < 3) {
            return null; //too short
        }

        if (userName.length() > 20) {
            return null; //too long
        }

        if (!StringUtils.hasText(userName)) {
            return null; //empty
        }
        Optional<UserEntity> foundUser = checkAndGetkUser(id, requester_id);
        if (foundUser.isPresent()) {
            foundUser.get().setName(userName);
            var savedUser = userRepository.saveAndFlush(foundUser.get());
            return generateUserResponseFromEntity(savedUser);
        } else {
            return null;
        }
    }

    public UserResponse updateTelephoneNumber(Long id, String telephoneNumber, Long requester_id) { //phone checks? some lib maybe?
        if (!StringUtils.hasText(telephoneNumber)) {
            return null; //empty
        }

        Optional<UserEntity> foundUser = checkAndGetkUser(id, requester_id);

        if (foundUser.isPresent()) {
            foundUser.get().setTelephoneNumber(telephoneNumber);
            var savedUser = userRepository.saveAndFlush(foundUser.get());
            return generateUserResponseFromEntity(savedUser);
        } else {
            return null;
        }
    }

    public UserResponse updateEmail(Long id, String email, Long requester_id) { //TODO: mail checks
        if (!StringUtils.hasText(email)) {
            return null; //empty
        }
        Optional<UserEntity> foundUser = checkAndGetkUser(id, requester_id);

        if (foundUser.isPresent()) {
            foundUser.get().setEmail(email);
            var savedUser = userRepository.saveAndFlush(foundUser.get());
            return generateUserResponseFromEntity(savedUser);
        } else {
            return null;
        }
    }

    public Boolean deleteSingleUser(Long id_to_delete, Long requester_id) {
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (!isRequesterIDValid(requesterUser, id_to_delete)) {
            return false;
        }

        if (userRepository.existsById(id_to_delete)) {
            userRepository.deleteById(id_to_delete);
            return true;
        }
        return false;
    }


}
