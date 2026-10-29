package com.cravedash.order.controller;

import com.cravedash.order.dto.CreateOrderRequest;
import com.cravedash.order.dto.OrderResponse;
import com.cravedash.order.entity.Order;
import com.cravedash.order.entity.OrderStatus;
import com.cravedash.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    private String getCallerEmail(HttpServletRequest request) {
        String email = (String) request.getAttribute("userEmail");
        if (email == null || email.isBlank()) {
            email = request.getHeader("X-User-Email");
        }
        return email != null ? email.trim() : "";
    }

    private String getCallerRole(HttpServletRequest request) {
        String role = (String) request.getAttribute("userRole");
        if (role == null || role.isBlank()) {
            role = request.getHeader("X-User-Role");
        }
        return role != null ? role.trim() : "";
    }

    // ==========================================
    // INSERTION: Place Multi-Item Order (Orchestration Engine)
    // ==========================================
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request, HttpServletRequest httpRequest) {
        try {
            // Set user email from authenticated context if missing or mismatched
            String callerEmail = getCallerEmail(httpRequest);
            if (!callerEmail.isBlank() && (request.getUserEmail() == null || request.getUserEmail().isBlank())) {
                request.setUserEmail(callerEmail);
            }
            OrderResponse response = orderService.createOrder(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Order placement failed: " + e.getMessage()));
        }
    }

    // IDOR Protected: Customer gets only their own orders; Owner gets all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(HttpServletRequest httpRequest) {
        String callerEmail = getCallerEmail(httpRequest);
        String callerRole = getCallerRole(httpRequest);
        return ResponseEntity.ok(orderService.getAllOrders(callerEmail, callerRole));
    }

    // IDOR Protected: View order by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable("id") Long id, HttpServletRequest httpRequest) {
        try {
            String callerEmail = getCallerEmail(httpRequest);
            String callerRole = getCallerRole(httpRequest);
            return ResponseEntity.ok(orderService.getOrderById(id, callerEmail, callerRole));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Access Denied")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // IDOR Protected: Query orders by user email
    @GetMapping("/user/{email}")
    public ResponseEntity<?> getOrdersByUser(@PathVariable("email") String email, HttpServletRequest httpRequest) {
        try {
            String callerEmail = getCallerEmail(httpRequest);
            String callerRole = getCallerRole(httpRequest);
            return ResponseEntity.ok(orderService.getOrdersByUser(email, callerEmail, callerRole));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Access Denied")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // ==========================================
    // DELETION: Cancel & Delete Order (IDOR Protected)
    // ==========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelAndDeleteOrder(@PathVariable("id") Long id, HttpServletRequest httpRequest) {
        try {
            String callerEmail = getCallerEmail(httpRequest);
            String callerRole = getCallerRole(httpRequest);
            orderService.cancelAndDeleteOrder(id, callerEmail, callerRole);
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("message", "Order ID " + id + " and related line items deleted from PostgreSQL mydb");
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Access Denied")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // RBAC Protected: Only Restaurant Owners can advance order statuses
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Long id, @RequestParam("status") OrderStatus status, HttpServletRequest httpRequest) {
        try {
            String callerRole = getCallerRole(httpRequest);
            Order updated = orderService.updateOrderStatus(id, status, callerRole);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Access Denied")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("service", "order-service", "status", "UP", "port", 8083));
    }
}
