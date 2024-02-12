package com.develhope.spring.entities.operations;

public enum PaymentStatus {
    PAID("Paid"),
    NOT_PAID("Not paid"),
    DEPOSIT("Deposit");
    private String status;
    private PaymentStatus(String a) {
        this.status = a;
    }

    public static boolean isValidPaymentStatus(String str) {
        try {
            com.develhope.spring.entities.operations.PaymentStatus.valueOf(str);
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