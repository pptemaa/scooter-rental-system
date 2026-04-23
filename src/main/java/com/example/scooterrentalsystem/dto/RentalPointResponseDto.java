package com.example.scooterrentalsystem.dto;

import java.math.BigDecimal;
import java.util.Map;

public record RentalPointResponseDto(
        Long id,
        String name,
        BigDecimal latitude,
        BigDecimal longitude,
        Long parentId,
        long totalScooters,
        Map<String, Long> modelCount,
        Map<String, Long> statusCount
) {
}
