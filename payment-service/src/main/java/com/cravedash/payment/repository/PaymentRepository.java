package com.cravedash.payment.repository;

import com.cravedash.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(Long orderId);
    Optional<Payment> findByTransactionRef(String transactionRef);
    List<Payment> findByUserEmail(String userEmail);
    void deleteByOrderId(Long orderId);
}
