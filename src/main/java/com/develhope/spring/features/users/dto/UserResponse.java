package com.develhope.spring.features.users.dto;

import com.develhope.spring.features.users.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String surname;
    private String telephoneNumber;
    private String email;
    private Role role;
}
