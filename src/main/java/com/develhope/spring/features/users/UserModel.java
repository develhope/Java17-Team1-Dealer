package com.develhope.spring.features.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    private Long id;

    private String name;

    private String surname;

    private String telephoneNumber;

    private String email;

    private String password;
}