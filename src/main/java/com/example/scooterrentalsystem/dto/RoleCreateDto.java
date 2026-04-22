package com.example.scooterrentalsystem.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleCreateDto(
        @NotBlank(message = "Имя роли не может быть пустым") String name
) {
}
