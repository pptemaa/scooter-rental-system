package com.example.scooterrentalsystem.dto;

import java.math.BigDecimal;

public record ScooterResponseDto(
        Long id,
        String modelName,
        String rentalPointName,
        String status,
        BigDecimal mileage
) {
}
