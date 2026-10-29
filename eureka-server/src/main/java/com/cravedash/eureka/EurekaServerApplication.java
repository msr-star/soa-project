package com.cravedash.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * PS034: CraveDash Mobility Services
 * Central Service Discovery Registry (Netflix Eureka)
 * 
 * Provides dynamic instance discovery, heartbeat monitoring, and registry tracking
 * for all CraveDash microservices.
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
