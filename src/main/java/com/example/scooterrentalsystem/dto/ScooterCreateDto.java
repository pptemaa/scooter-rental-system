package com.example.scooterrentalsystem.dto;

import jakarta.validation.constraints.NotNull;

public record ScooterCreateDto(
        @NotNull(message = "modelId обязателен")
        Long modelId,
        Long rentalPointId
) {
}
