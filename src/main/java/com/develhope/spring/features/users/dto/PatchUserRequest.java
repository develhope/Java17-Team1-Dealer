package com.develhope.spring.features.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchUserRequest {
    private String name;
    private String surname;
    private String telephoneNumber;
    private String email;
    private String password;
    private String role;
}

