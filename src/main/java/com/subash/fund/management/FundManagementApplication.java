package com.subash.fund.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Fund Management microservice application.
 * <p>
 * This class bootstraps the Spring Boot application, enabling component scanning,
 * auto-configuration, and Spring application context setup.
 * </p>
 *
 * <p>Use this class to run the application as a standalone Spring Boot app.</p>
 */
@SpringBootApplication
public class FundManagementApplication {

    /**
     * Main method that launches the Spring Boot application.
     *
     * @param args Command-line arguments passed during application startup.
     */
    public static void main(String[] args) {
        SpringApplication.run(FundManagementApplication.class, args);
    }

}
