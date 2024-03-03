package com.develhope.spring.features.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchUserRequest {
    String name;
    String surname;
    String telephoneNumber;
    String email;

    String password;
    String userType;
}

