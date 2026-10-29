package com.cravedash.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * PS034: CraveDash Mobility Services
 * Central API Gateway Microservice
 * 
 * Provides centralized entrypoint routing (port 8080), dynamic Eureka
 * service discovery load balancing, CORS pre-flight negotiation, and JWT security filtering.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
