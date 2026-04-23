package com.example.scooterrentalsystem.dto;

import com.example.scooterrentalsystem.entity.TariffType;

import java.math.BigDecimal;

public record TariffResponseDto (
    Long id,
    String name,
    TariffType type,
    BigDecimal price,
    BigDecimal discount
){}
