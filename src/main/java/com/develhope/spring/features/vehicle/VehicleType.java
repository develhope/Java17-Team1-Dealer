package com.develhope.spring.features.vehicle;

public enum VehicleType {

    CAR("Car"),
    MOTORBIKE("Motorbike"),
    SCOOTER("Scooter"),
    VAN("Van");
    private String type;

    private VehicleType(String a) {
        this.type = a;
    }

    public static boolean isValidVehicleType(String str) {
        try {
            VehicleType.valueOf(str);
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