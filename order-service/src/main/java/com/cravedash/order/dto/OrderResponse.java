package com.cravedash.order.dto;

import com.cravedash.order.entity.OrderItem;
import com.cravedash.order.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long id;
    private String orderNumber;
    private String userEmail;
    private Long restaurantId;
    private String restaurantName;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String deliveryAddress;
    private String paymentMethod;
    private String paymentTxRef;
    private LocalDateTime createdAt;
    private List<OrderItem> items;
    private String message;

    public OrderResponse() {
    }

    public OrderResponse(Long id, String orderNumber, String userEmail, Long restaurantId, String restaurantName, BigDecimal totalAmount, OrderStatus status, String deliveryAddress, String paymentMethod, String paymentTxRef, LocalDateTime createdAt, List<OrderItem> items, String message) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.userEmail = userEmail;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.totalAmount = totalAmount;
        this.status = status;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.paymentTxRef = paymentTxRef;
        this.createdAt = createdAt;
        this.items = items;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentTxRef() {
        return paymentTxRef;
    }

    public void setPaymentTxRef(String paymentTxRef) {
        this.paymentTxRef = paymentTxRef;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
