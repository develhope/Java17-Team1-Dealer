package com.develhope.spring.features.users.dto;

import com.develhope.spring.features.users.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    Long id;
    String name;
    String surname;
    String telephoneNumber;
    String email;
    String password;
    UserType userType;
}
