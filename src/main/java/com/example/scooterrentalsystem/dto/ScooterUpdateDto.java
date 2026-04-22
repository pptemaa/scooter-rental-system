package com.example.scooterrentalsystem.dto;

import com.example.scooterrentalsystem.entity.Status;

public record ScooterUpdateDto(
        Long modelId,
        Long rentalPointId,
        Status status
) {
}
