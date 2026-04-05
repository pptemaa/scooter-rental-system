package com.example.scooterrentalsystem.entity;

public enum Status {
    AVAILABLE, // Свободен, стоит на парковке
    RENTED,    // Сейчас находится в аренде
    BROKEN,    // Сломан, требует ремонта
    MAINTENANCE // На плановом техобслуживании
}
