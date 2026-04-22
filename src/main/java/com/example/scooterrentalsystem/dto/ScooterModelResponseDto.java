package com.example.scooterrentalsystem.dto;

public record ScooterModelResponseDto(
        Long id,
        String name,
        Integer maxWeight
) {
}
