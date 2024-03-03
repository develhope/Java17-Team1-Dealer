package com.develhope.spring.features.users;

import com.develhope.spring.features.users.dto.UserResponse;
import com.develhope.spring.features.users.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserEntity convertUserRequestToEntity(CreateUserRequest createUserRequest) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(createUserRequest, UserEntity.class);
    }

    public UserResponse convertUserEntityToResponse(UserEntity userEntity) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(userEntity, UserResponse.class);
    }
}
