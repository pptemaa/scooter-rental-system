package com.example.scooterrentalsystem.dto;

import java.math.BigDecimal;

public record UserResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String roleName,
        BigDecimal balance
) {
}
