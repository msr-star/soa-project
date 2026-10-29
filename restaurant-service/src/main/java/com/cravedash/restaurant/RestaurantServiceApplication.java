package com.cravedash.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * PS034: CraveDash Mobility Services
 * Restaurant & Menu Microservice
 * 
 * Provides dynamic menu catalogs, real-time inventory toggle,
 * and direct PostgreSQL (mydb) INSERT and DELETE operations.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class RestaurantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantServiceApplication.class, args);
    }
}
