package com.example.scooterrentalsystem.dto;

import jakarta.validation.constraints.NotBlank;

public record ScooterModelCreateDto(
        @NotBlank String name,
        Integer maxWeight
) {
}
