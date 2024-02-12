package com.develhope.spring.users;

import lombok.Data;

@Data
public class User {
    private String name;
    private String surname;
    private String telephoneNumber;
    private String email;
    private String password;
}
