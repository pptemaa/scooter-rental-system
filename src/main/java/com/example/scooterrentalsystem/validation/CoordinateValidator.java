package com.example.scooterrentalsystem.validation;

import java.math.BigDecimal;

public final class CoordinateValidator {

    private static final BigDecimal LAT_MIN = BigDecimal.valueOf(-90);
    private static final BigDecimal LAT_MAX = BigDecimal.valueOf(90);
    private static final BigDecimal LON_MIN = BigDecimal.valueOf(-180);
    private static final BigDecimal LON_MAX = BigDecimal.valueOf(180);

    private CoordinateValidator() {
    }

    public static void requireValidCoordinates(BigDecimal latitude, BigDecimal longitude) {
        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException("Широта и долгота обязательны и не могут быть null");
        }
        if (latitude.compareTo(LAT_MIN) < 0 || latitude.compareTo(LAT_MAX) > 0) {
            throw new IllegalArgumentException("Широта должна быть в диапазоне от -90 до 90 градусов");
        }
        if (longitude.compareTo(LON_MIN) < 0 || longitude.compareTo(LON_MAX) > 0) {
            throw new IllegalArgumentException("Долгота должна быть в диапазоне от -180 до 180 градусов");
        }
    }
}
