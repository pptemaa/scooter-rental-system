package com.example.scooterrentalsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record UserCreateDto(
        @NotBlank @Email String email,
        @NotBlank String password,
        String firstName,
        String lastName,
        @NotBlank String roleName,
        BigDecimal balance
) {
}
