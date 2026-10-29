package com.cravedash.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * PS034: CraveDash Mobility Services
 * Auth & Identity Microservice
 * 
 * Provides email authentication, BCrypt password hashing,
 * JWT token issuance with role-based claims, and user lifecycle management.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
