package com.restaurant.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@EnableCaching
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class ResturantManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResturantManagementSystemApplication.class, args);
    }

}
