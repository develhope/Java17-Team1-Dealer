package com.develhope.spring.Vehicles;

public enum VehicleStatusEnum {

    UNAVAILABLE("Unavailable"),
    CAN_BE_ORDERED("Available for order"),
    PROMPT_DELIVERY("Available for prompt delivery");
    private String status;
    private VehicleStatusEnum(String a) {
        this.status = a;
    }

    public static boolean isValidVehicleStatus(String str) {
        try {
            VehicleStatusEnum.valueOf(str);
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