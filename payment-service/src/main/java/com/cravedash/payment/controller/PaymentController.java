package com.cravedash.payment.controller;

import com.cravedash.payment.dto.PaymentRequest;
import com.cravedash.payment.dto.PaymentResponse;
import com.cravedash.payment.entity.Payment;
import com.cravedash.payment.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    private boolean isAuthorizedOwner(HttpServletRequest request) {
        String role = (String) request.getAttribute("userRole");
        if (role == null || role.isBlank()) {
            role = request.getHeader("X-User-Role");
        }
        return "RESTAURANT_OWNER".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role) || "INTERNAL_SERVICE".equalsIgnoreCase(role);
    }

    // ==========================================
    // INSERTION: Charge and record payment
    // ==========================================
    @PostMapping("/charge")
    public ResponseEntity<?> charge(@RequestBody PaymentRequest request) {
        try {
            PaymentResponse response = paymentService.charge(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getPaymentByOrderId(@PathVariable("orderId") Long orderId) {
        try {
            return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // RBAC: Audit full payment ledger (Owner only)
    @GetMapping
    public ResponseEntity<?> getAllPayments(HttpServletRequest httpRequest) {
        if (!isAuthorizedOwner(httpRequest)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Access Denied: RESTAURANT_OWNER role required to audit payment ledger"));
        }
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    // ==========================================
    // DELETION: Purge / Refund Transaction (RBAC: Owner only)
    // ==========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable("id") Long id, HttpServletRequest httpRequest) {
        if (!isAuthorizedOwner(httpRequest)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Access Denied: RESTAURANT_OWNER role required to purge payment records"));
        }
        try {
            paymentService.deletePayment(id);
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("message", "Payment transaction ID " + id + " purged from PostgreSQL mydb");
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<?> deletePaymentByOrderId(@PathVariable("orderId") Long orderId) {
        paymentService.deletePaymentByOrderId(orderId);
        return ResponseEntity.ok(Map.of("success", true, "message", "Payment for order " + orderId + " deleted"));
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("service", "payment-service", "status", "UP", "port", 8084));
    }
}
