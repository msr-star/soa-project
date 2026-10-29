package com.cravedash.order.client;

import com.cravedash.order.client.dto.PaymentChargeRequest;
import com.cravedash.order.client.dto.PaymentChargeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/api/payments/charge")
    PaymentChargeResponse charge(@RequestBody PaymentChargeRequest request);

    @DeleteMapping("/api/payments/order/{orderId}")
    Map<String, Object> deletePaymentByOrderId(@PathVariable("orderId") Long orderId);
}
