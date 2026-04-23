package com.example.scooterrentalsystem.dto;

import com.example.scooterrentalsystem.entity.TariffType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record TariffCreateDto(
        @NotBlank(message = "Название тарифа не может быть пустым")
        String name,

        @NotNull(message = "Тип тарифа не может быть пустым")
        TariffType type,

        @NotNull(message = "Цена должна быть указана")
        @PositiveOrZero(message = "Цена не может быть отрицательной")
        BigDecimal price,
        @NotNull(message = "Скидка должна быть указана")
        @PositiveOrZero(message = "Скидка не может быть отрицательной")
        BigDecimal discount
){}
