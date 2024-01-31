package com.develhope.spring.Users;

public enum UserTypeEnum {

    CUSTOMER("Customer"),
    SELLER("Seller"),
    ADMIN("Administrator");

    private String type;

    private UserTypeEnum(String a) {
        this.type = a;
    }

    public static boolean isValidUserType(String str) {
        try {
            UserTypeEnum.valueOf(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return type;
    }
}