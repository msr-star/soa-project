package com.cravedash.payment.service;

import com.cravedash.payment.dto.PaymentRequest;
import com.cravedash.payment.dto.PaymentResponse;
import com.cravedash.payment.entity.Payment;
import com.cravedash.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // ==========================================
    // INSERTION: Process & Store Payment in PostgreSQL mydb
    // ==========================================
    @Transactional
    public PaymentResponse charge(PaymentRequest req) {
        String txRef = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        String method = req.getPaymentMethod() != null ? req.getPaymentMethod().trim() : "UPI";
        boolean simulateFailure = "FAIL".equalsIgnoreCase(method) 
                || "SIMULATED_FAILURE".equalsIgnoreCase(method)
                || (req.getAmount() != null && req.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0);

        String status = simulateFailure ? "FAILED" : "SUCCESS";
        String message = simulateFailure 
                ? "Payment failed: Card declined or simulated gateway rejection."
                : "Payment of ₹" + req.getAmount() + " authorized successfully via " + method;

        Payment payment = new Payment(
                txRef,
                req.getOrderId(),
                req.getUserEmail() != null ? req.getUserEmail() : "guest@cravedash.com",
                req.getAmount(),
                method,
                status
        );

        Payment saved = paymentRepository.save(payment);

        return new PaymentResponse(
                saved.getId(),
                saved.getTransactionRef(),
                saved.getOrderId(),
                saved.getUserEmail(),
                saved.getAmount(),
                saved.getPaymentMethod(),
                saved.getStatus(),
                saved.getCreatedAt(),
                message
        );
    }

    public Payment getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("No payment found for order ID: " + orderId));
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // ==========================================
    // DELETION: Purge / Refund Transaction from PostgreSQL mydb
    // ==========================================
    @Transactional
    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new IllegalArgumentException("Payment record not found with ID: " + id);
        }
        paymentRepository.deleteById(id);
    }

    @Transactional
    public void deletePaymentByOrderId(Long orderId) {
        paymentRepository.deleteByOrderId(orderId);
    }
}
