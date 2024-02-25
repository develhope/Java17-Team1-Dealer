package com.develhope.spring.features.users;

import com.develhope.spring.features.users.dto.UserResponse;
import com.develhope.spring.features.users.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserModel convertUserRequestToModel(CreateUserRequest createUserRequest) {
        return modelMapper.map(createUserRequest, UserModel.class);
    }

    public UserEntity convertUserModelToEntity(UserModel userModel) {
        return modelMapper.map(userModel, UserEntity.class);
    }

    public UserModel convertUserEntityToModel(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserModel.class);
    }

    public UserResponse convertUserModelToResponse(UserModel userModel) {
        return modelMapper.map(userModel, UserResponse.class);
    }
}
