package com.cravedash.order.client.dto;

import java.math.BigDecimal;

public class PaymentChargeResponse {
    private Long id;
    private String transactionRef;
    private Long orderId;
    private BigDecimal amount;
    private String status;
    private String message;

    public PaymentChargeResponse() {
    }

    public PaymentChargeResponse(Long id, String transactionRef, Long orderId, BigDecimal amount, String status, String message) {
        this.id = id;
        this.transactionRef = transactionRef;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
