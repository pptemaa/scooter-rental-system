package com.example.scooterrentalsystem.dto;

import java.math.BigDecimal;

public record RentalPointResponseDto(
        Long id,
        String name,
        BigDecimal latitude,
        BigDecimal longitude,
        Long parentId
) {
}
