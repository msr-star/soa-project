package com.cravedash.payment.dto;

import java.math.BigDecimal;

public class PaymentRequest {
    private Long orderId;
    private String userEmail;
    private BigDecimal amount;
    private String paymentMethod;

    public PaymentRequest() {
    }

    public PaymentRequest(Long orderId, String userEmail, BigDecimal amount, String paymentMethod) {
        this.orderId = orderId;
        this.userEmail = userEmail;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
