package com.develhope.spring.features.orders;

public enum PaymentStatus {
    PAID("Paid"),
    NOT_PAID("Not paid"),
    PENDING("Pending"),
    DEPOSIT("Deposit");
    private String status;

    private PaymentStatus(String a) {
        this.status = a;
    }

    public static boolean isValidPaymentStatus(String str) {
        try {
            PaymentStatus.valueOf(str);
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