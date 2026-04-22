package com.example.scooterrentalsystem.dto;

public record UserResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String roleName
) {
}
