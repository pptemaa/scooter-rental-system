package com.example.scooterrentalsystem.dto;

import java.math.BigDecimal;

public record TariffResponseDto (
    Long id,
    String name,
    String type,
    BigDecimal price,
    BigDecimal discount
){}
