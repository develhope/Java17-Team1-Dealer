package com.develhope.spring.Users;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Users {
    private String name;
    private String surname;
    private String telephoneNumber;
    private String email;
    private String password;
    private UserTypeEnum userType = UserTypeEnum.CUSTOMER;
}
