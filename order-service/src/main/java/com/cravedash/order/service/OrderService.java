package com.cravedash.order.service;

import com.cravedash.order.client.PaymentClient;
import com.cravedash.order.client.RestaurantClient;
import com.cravedash.order.client.dto.MenuItemDto;
import com.cravedash.order.client.dto.PaymentChargeRequest;
import com.cravedash.order.client.dto.PaymentChargeResponse;
import com.cravedash.order.dto.CartItemRequest;
import com.cravedash.order.dto.CreateOrderRequest;
import com.cravedash.order.dto.OrderResponse;
import com.cravedash.order.entity.Order;
import com.cravedash.order.entity.OrderItem;
import com.cravedash.order.entity.OrderStatus;
import com.cravedash.order.repository.OrderItemRepository;
import com.cravedash.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final RestaurantClient restaurantClient;
    private final PaymentClient paymentClient;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, RestaurantClient restaurantClient, PaymentClient paymentClient) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.restaurantClient = restaurantClient;
        this.paymentClient = paymentClient;
    }

    // ==========================================
    // INSERTION: Multi-Item Order Orchestration (Order -> Restaurant -> Payment)
    // ==========================================
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest req) {
        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart cannot be empty");
        }

        String orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        BigDecimal totalAmount = BigDecimal.ZERO;

        List<OrderItem> itemsToSave = new ArrayList<>();

        for (CartItemRequest itemReq : req.getItems()) {
            String itemName = "Dish #" + itemReq.getMenuItemId();
            BigDecimal unitPrice = new BigDecimal("199.00");

            // Inter-Service Communication 1: Call Restaurant Service via Feign
            try {
                MenuItemDto menuItem = restaurantClient.getMenuItemById(itemReq.getMenuItemId());
                if (menuItem != null) {
                    if (Boolean.FALSE.equals(menuItem.getIsAvailable())) {
                        throw new IllegalStateException("Dish '" + menuItem.getName() + "' is currently out of stock!");
                    }
                    itemName = menuItem.getName();
                    unitPrice = menuItem.getPrice();
                }
            } catch (IllegalStateException ise) {
                throw ise;
            } catch (Exception e) {
                logger.warn("Could not query restaurant-service for item {}, using fallback: {}", itemReq.getMenuItemId(), e.getMessage());
            }

            int qty = itemReq.getQuantity() != null && itemReq.getQuantity() > 0 ? itemReq.getQuantity() : 1;
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(qty));
            totalAmount = totalAmount.add(subtotal);

            OrderItem oi = new OrderItem(null, itemReq.getMenuItemId(), itemName, unitPrice, qty, subtotal);
            itemsToSave.add(oi);
        }

        // 1. INSERT into PostgreSQL mydb 'orders' table
        Order order = new Order(
                orderNumber,
                req.getUserEmail() != null ? req.getUserEmail() : "guest@cravedash.com",
                req.getRestaurantId() != null ? req.getRestaurantId() : 1L,
                req.getRestaurantName() != null ? req.getRestaurantName() : "Partner Kitchen",
                totalAmount,
                OrderStatus.PENDING,
                req.getDeliveryAddress() != null ? req.getDeliveryAddress() : "Hostel Block A, Campus Road",
                req.getPaymentMethod() != null ? req.getPaymentMethod() : "UPI",
                null
        );

        Order savedOrder = orderRepository.save(order);

        // 2. INSERT into PostgreSQL mydb 'order_items' table
        for (OrderItem oi : itemsToSave) {
            oi.setOrderId(savedOrder.getId());
            orderItemRepository.save(oi);
        }

        // Inter-Service Communication 2: Call Payment Service via Feign
        String txRef = "PENDING";
        try {
            PaymentChargeRequest payReq = new PaymentChargeRequest(
                    savedOrder.getId(),
                    savedOrder.getUserEmail(),
                    savedOrder.getTotalAmount(),
                    savedOrder.getPaymentMethod()
            );
            PaymentChargeResponse payResp = paymentClient.charge(payReq);
            if (payResp != null && "SUCCESS".equalsIgnoreCase(payResp.getStatus())) {
                txRef = payResp.getTransactionRef();
                savedOrder.setStatus(OrderStatus.CONFIRMED);
            } else {
                txRef = payResp != null && payResp.getTransactionRef() != null ? payResp.getTransactionRef() : "FAILED";
                savedOrder.setStatus(OrderStatus.PAYMENT_FAILED);
                logger.warn("Payment authorization returned non-success for order {}: {}", savedOrder.getId(), payResp != null ? payResp.getStatus() : "null");
            }
        } catch (Exception e) {
            logger.error("Payment service Feign call failed for order {}: {}", savedOrder.getId(), e.getMessage());
            txRef = "FAILED";
            savedOrder.setStatus(OrderStatus.PAYMENT_FAILED);
        }

        savedOrder.setPaymentTxRef(txRef);
        Order finalized = orderRepository.save(savedOrder);

        List<OrderItem> savedItems = orderItemRepository.findByOrderId(finalized.getId());
        String userMessage = finalized.getStatus() == OrderStatus.CONFIRMED
                ? "Order placed and authorized successfully! Order ID: " + finalized.getOrderNumber()
                : "Payment authorization failed. Order has been recorded as PAYMENT_FAILED.";

        return new OrderResponse(
                finalized.getId(),
                finalized.getOrderNumber(),
                finalized.getUserEmail(),
                finalized.getRestaurantId(),
                finalized.getRestaurantName(),
                finalized.getTotalAmount(),
                finalized.getStatus(),
                finalized.getDeliveryAddress(),
                finalized.getPaymentMethod(),
                finalized.getPaymentTxRef(),
                finalized.getCreatedAt(),
                savedItems,
                userMessage
        );
    }

    // ==========================================
    // DELETION: Cancel & Permanently Delete Order (IDOR Protected)
    // ==========================================
    @Transactional
    public void cancelAndDeleteOrder(Long id, String callerEmail, String callerRole) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));

        boolean isOwner = isOwnerRole(callerRole);
        if (!isOwner && (callerEmail == null || !order.getUserEmail().equalsIgnoreCase(callerEmail.trim()))) {
            throw new IllegalArgumentException("Access Denied: You cannot delete or cancel orders placed by another user!");
        }

        // Cleanup payments via Feign
        try {
            paymentClient.deletePaymentByOrderId(id);
        } catch (Exception e) {
            logger.warn("Payment refund/deletion skipped: {}", e.getMessage());
        }

        orderItemRepository.deleteByOrderId(id);
        orderRepository.deleteById(id);
    }

    // IDOR Protected: View order by ID
    public OrderResponse getOrderById(Long id, String callerEmail, String callerRole) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));

        boolean isOwner = isOwnerRole(callerRole);
        if (!isOwner && (callerEmail == null || !order.getUserEmail().equalsIgnoreCase(callerEmail.trim()))) {
            throw new IllegalArgumentException("Access Denied: You cannot view orders placed by another user!");
        }

        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getUserEmail(),
                order.getRestaurantId(),
                order.getRestaurantName(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getDeliveryAddress(),
                order.getPaymentMethod(),
                order.getPaymentTxRef(),
                order.getCreatedAt(),
                items,
                "Order details retrieved successfully"
        );
    }

    // IDOR Protected: Customer gets only their own orders; Owner gets all orders
    public List<Order> getAllOrders(String callerEmail, String callerRole) {
        if (isOwnerRole(callerRole)) {
            return orderRepository.findAllByOrderByCreatedAtDesc();
        }
        if (callerEmail != null && !callerEmail.isBlank()) {
            return orderRepository.findByUserEmailOrderByCreatedAtDesc(callerEmail);
        }
        return List.of();
    }

    // IDOR Protected: Query orders for a user email
    public List<Order> getOrdersByUser(String targetEmail, String callerEmail, String callerRole) {
        if (!isOwnerRole(callerRole)) {
            if (callerEmail == null || !targetEmail.equalsIgnoreCase(callerEmail.trim())) {
                throw new IllegalArgumentException("Access Denied: You cannot view order history for other users!");
            }
        }
        return orderRepository.findByUserEmailOrderByCreatedAtDesc(targetEmail);
    }

    // RBAC: Only Restaurant Owners can advance order statuses
    @Transactional
    public Order updateOrderStatus(Long id, OrderStatus newStatus, String callerRole) {
        if (!isOwnerRole(callerRole)) {
            throw new IllegalArgumentException("Access Denied: RESTAURANT_OWNER role required to update order fulfillment status!");
        }
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    private boolean isOwnerRole(String role) {
        return "RESTAURANT_OWNER".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role) || "INTERNAL_SERVICE".equalsIgnoreCase(role);
    }
}
