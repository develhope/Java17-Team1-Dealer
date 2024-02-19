package com.develhope.spring.entities.users;

public enum UserType {

    ADMIN("Admin"),
    SELLER("Seller"),
    BUYER("Buyer");
    private String type;

    private UserType(String a) {
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