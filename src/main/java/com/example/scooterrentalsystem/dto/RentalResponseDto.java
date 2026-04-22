package com.example.scooterrentalsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RentalResponseDto(
        Long id,
        Long userId,
        String userEmail,
        Long scooterId,
        String scooterModelName,
        Long tariffId,
        String tariffName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Long startPointId,
        Long endPointId,
        BigDecimal totalCost
) {
}
