package com.develhope.spring.features.users;

public enum Role {

    ADMIN("Admin"),
    SELLER("Seller"),
    CUSTOMER("Customer"),
    NOT_SET("Not Set");
    private final String role;

    Role(String a) {
        this.role = a;
    }

    public static boolean isValidUserRole(String str) {
        try {
            Role.valueOf(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return role;
    }
}