package com.example.scooterrentalsystem.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RentalFinishDto(
        @NotNull Long userId,
        @NotNull Long endPointId,
        @NotNull BigDecimal distanceKm
) {
}
