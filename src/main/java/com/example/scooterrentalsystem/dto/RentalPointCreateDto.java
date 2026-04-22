package com.example.scooterrentalsystem.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RentalPointCreateDto(
        @NotBlank(message = "Название точки не может быть пустым")
        String name,

        @NotNull(message = "Широта обязательна")
        @DecimalMin(value = "-90.0", inclusive = true, message = "Широта должна быть от -90 до 90")
        @DecimalMax(value = "90.0", inclusive = true, message = "Широта должна быть от -90 до 90")
        BigDecimal latitude,

        @NotNull(message = "Долгота обязательна")
        @DecimalMin(value = "-180.0", inclusive = true, message = "Долгота должна быть от -180 до 180")
        @DecimalMax(value = "180.0", inclusive = true, message = "Долгота должна быть от -180 до 180")
        BigDecimal longitude,

        Long parentId
) {
}
