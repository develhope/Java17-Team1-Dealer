package com.develhope.spring.features.orders;

public enum OrderStatus {
    TO_SEND("To send"),
    SHIPPED("Shipped"),
    DELIVERING("Delivering"),
    DELIVERED("Delivered");
    private String status;

    private OrderStatus(String a) {
        this.status = a;
    }

    public static boolean isValidOrderStatus(String str) {
        try {
            OrderStatus.valueOf(str);
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
