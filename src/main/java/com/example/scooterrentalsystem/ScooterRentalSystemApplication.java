package com.example.scooterrentalsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ScooterRentalSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScooterRentalSystemApplication.class, args);
    }

}
