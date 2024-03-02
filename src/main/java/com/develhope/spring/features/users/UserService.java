package com.develhope.spring.features.users;

import com.develhope.spring.features.users.dto.UserResponse;

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
    

    private UserResponse generateUserResponseFromEntity(UserEntity userEntity) {
        return userMapper.convertUserModelToResponse(userMapper.convertUserEntityToModel(userEntity));
    }

    private Boolean isRequesterIDValid(Optional<UserEntity> requesterUser, Long id) {
        if (requesterUser.isEmpty()) {
            return false;
        }

        if (requesterUser.get().getId() != id && requesterUser.get().getUserType() != UserType.ADMIN) {
            return false;
        }
        return true;
    }

    private UserEntity saveUserEntityFromUserModel(UserModel userModel){
        UserEntity userEntity = userMapper.convertUserModelToEntity(userModel);
        UserEntity userEntitySaved = userRepository.saveAndFlush(userEntity);
        return userEntitySaved;
    }
    public UserResponse createUser(UserModel userModel) {
        return generateUserResponseFromEntity(saveUserEntityFromUserModel(userModel));
    }

    public UserResponse getSingleUser(Long id) {
        var foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            return generateUserResponseFromEntity(foundUser.get());
        }
        return null;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    //TO DO: general functions for better checks (like, userNameCheck(string) -> bool)
    //right now these are simple, just checking empty/null
    public UserResponse patchUser(Long id, UserModel userModel) {
        Optional<UserEntity> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            if (StringUtils.hasText(userModel.getEmail())) {
                foundUser.get().setEmail(userModel.getEmail());
            }

            if (StringUtils.hasText(userModel.getName())) {
                foundUser.get().setName(userModel.getName());
            }

            if (StringUtils.hasText(userModel.getSurname())) {
                foundUser.get().setSurname(userModel.getSurname());
            }

            if (StringUtils.hasText(userModel.getTelephoneNumber())) {
                foundUser.get().setTelephoneNumber(userModel.getTelephoneNumber());
            }

            if (StringUtils.hasText(userModel.getPassword())) {
                foundUser.get().setPassword(userModel.getPassword());
            }
            var savedUser = userRepository.saveAndFlush(foundUser.get());
            return generateUserResponseFromEntity(savedUser);
        } else {
            return null;
        }
    }

    private Optional<UserEntity> getUser(Long searched_id, Long requester_id){ //TODO: put this check before
        Optional<UserEntity> requesterUser = userRepository.findById(requester_id);
        if (!isRequesterIDValid(requesterUser, searched_id)) {
            return Optional.empty();
        }

        return requester_id == searched_id ? requesterUser : userRepository.findById(searched_id);
    }
    //TODO: proper returns for proper errors;
    public UserResponse updatePassword(Long id, String password, Long requester_id) { //already hashed
        Optional<UserEntity> foundUser = getUser(id, requester_id);

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
        Optional<UserEntity> foundUser = getUser(id, requester_id);

        if (foundUser.isPresent()) {
            if (userName.length() < 3) {
                return null; //too short
            }

            if (userName.length() > 20) {
                return null; //too long
            }

            if (!StringUtils.hasText(userName)) {
                return null; //empty
            }

            foundUser.get().setName(userName);
            var savedUser = userRepository.saveAndFlush(foundUser.get());
            return generateUserResponseFromEntity(savedUser);
        } else {
            return null;
        }
    }

    public UserResponse updateTelephoneNumber(Long id, String telephoneNumber, Long requester_id) { //phone checks? some lib maybe?
        Optional<UserEntity> foundUser = getUser(id, requester_id);
        
        if (foundUser.isPresent()) {
            if (!StringUtils.hasText(telephoneNumber)) {
                return null; //empty
            }

            foundUser.get().setTelephoneNumber(telephoneNumber);
            var savedUser = userRepository.saveAndFlush(foundUser.get());
            return generateUserResponseFromEntity(savedUser);
        } else {
            return null;
        }
    }

    public UserResponse updateEmail(Long id, String email, Long requester_id) { //TODO: mail checks
        Optional<UserEntity> foundUser = getUser(id, requester_id);

        if (foundUser.isPresent()) {
            if (!StringUtils.hasText(email)) {
                return null; //empty
            }

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

        if (userRepository.existsById(id_to_delete))
        {
            userRepository.deleteById(id_to_delete);
            return true;
        }
        return false;
    }


}
