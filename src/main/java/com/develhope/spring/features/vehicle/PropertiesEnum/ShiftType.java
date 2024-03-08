package com.develhope.spring.features.vehicle.PropertiesEnum;

public enum ShiftType {

    MANUAL("Manual"),
    SEMI_AUTOMATIC("Semi automatic"),
    AUTOMATIC("Automatic");
    private final String shiftType;

    ShiftType(String a) {
        this.shiftType = a;
    }

    public static boolean isValidShiftType(String str) {
        try {
            ShiftType.valueOf(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return shiftType;
    }
}