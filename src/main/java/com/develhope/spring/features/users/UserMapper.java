package com.develhope.spring.features.users;

import com.develhope.spring.features.users.dto.UserResponse;
import com.develhope.spring.features.vehicle.dto.CreateUserRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class UserMapper {
    @Autowired
    ModelMapper modelMapper;

    public UserModel convertUserRequestToModel (CreateUserRequest createUserRequest){
        return  modelMapper.map(createUserRequest,UserModel.class);
    }
    public UserEntity convertUserModelToEntity (UserModel userModel){
        return  modelMapper.map(userModel,UserEntity.class);
    }
    public UserModel convertUserEntityToModel (UserEntity userEntity){
        return modelMapper.map(userEntity, UserModel.class);
    }
    public UserResponse convertUserModelToResponse (UserModel userModel){
        return modelMapper.map(userModel, UserResponse.class);
    }
}
