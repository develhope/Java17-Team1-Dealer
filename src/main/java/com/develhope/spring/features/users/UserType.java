package com.develhope.spring.features.users;

public enum UserType {

    ADMIN("Admin"),
    SELLER("Seller"),
    CUSTOMER("Customer");
    private final String type;

    UserType(String a) {
        this.type = a;
    }

    public static boolean isValidUserType(String str) {
        try {
            UserType.valueOf(str);
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