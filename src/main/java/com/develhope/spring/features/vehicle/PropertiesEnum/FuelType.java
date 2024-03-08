package com.develhope.spring.features.vehicle.PropertiesEnum;

public enum FuelType {

    SUPER("Super"),
    PETROL("Petrol"),
    DIESEL("Diesel"),
    LPG("Liquefied petroleum gas (LPG)"),
    LPG_PETROL("LPG/Petrol Hybrid"),
    METHANE("Methane"),
    ELECTRIC("Electric");
    private final String fuelType;

    FuelType(String a) {
        this.fuelType = a;
    }

    public static boolean isValidFuelType(String str) {
        try {
            FuelType.valueOf(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return fuelType;
    }
}