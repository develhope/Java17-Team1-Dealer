package com.develhope.spring.features.vehicle;

public enum VehicleStatus {

    UNAVAILABLE("Unavailable"),
    CAN_BE_ORDERED("Available for order"),
    PROMPT_DELIVERY("Available for prompt delivery");
    private final String status;

    VehicleStatus(String a) {
        this.status = a;
    }

    public static boolean isValidVehicleStatus(String str) {
        try {
            VehicleStatus.valueOf(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return status;
    }
}