package com.develhope.spring.features.users.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class UserResponse {
    Long id;
    String name;
    String surname;
    String telephoneNumber;
    String email;

    String password;
}
