package com.example.scooterrentalsystem.dto;

import jakarta.validation.constraints.NotNull;

public record RentalStartDto(
        @NotNull Long userId,
        @NotNull Long scooterId,
        @NotNull Long tariffId
) {
}
