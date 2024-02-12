package com.develhope.spring.entities.operations;

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
            com.develhope.spring.entities.operations.OrderStatus.valueOf(str);
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
