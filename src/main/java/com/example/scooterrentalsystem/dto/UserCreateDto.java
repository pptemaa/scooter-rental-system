package com.example.scooterrentalsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateDto(
        @NotBlank @Email String email,
        @NotBlank String password,
        String firstName,
        String lastName,
        @NotBlank String roleName
) {
}
