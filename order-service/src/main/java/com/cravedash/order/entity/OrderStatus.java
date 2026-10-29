package com.cravedash.order.entity;

public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PAYMENT_FAILED,
    PREPARING,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED
}
