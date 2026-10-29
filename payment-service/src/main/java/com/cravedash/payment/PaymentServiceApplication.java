package com.cravedash.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * PS034: CraveDash Mobility Services
 * Payment Processing Microservice
 * 
 * Manages tokenized payment transactions, transaction verification,
 * and direct PostgreSQL (mydb) INSERT and DELETE ledger operations.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }
}
