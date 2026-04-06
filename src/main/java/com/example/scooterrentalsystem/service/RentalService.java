package com.example.scooterrentalsystem.service;

import com.example.scooterrentalsystem.dao.*;
import com.example.scooterrentalsystem.entity.*;
import com.example.scooterrentalsystem.exeption.BusinessLogicException;
import com.example.scooterrentalsystem.exeption.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RentalService {
    private final RentalDao rentalDao;
    private final RentalPointDao rentalPointDao;
    private final ScooterDao scooterDao;
    private final TariffDao tariffDao;
    private final UserDao userDao;

    public RentalService(RentalDao rentalDao, RentalPointDao rentalPointDao, ScooterDao scooterDao, TariffDao tariffDao, UserDao userDao) {
        this.rentalDao = rentalDao;
        this.rentalPointDao = rentalPointDao;
        this.scooterDao = scooterDao;
        this.tariffDao = tariffDao;
        this.userDao = userDao;
    }

    public Rental startRental(Long userId, Long scooterId, Long tariffId) {
        if (rentalDao.findActiveRentalByUserId(userId).isPresent()) {
            throw new BusinessLogicException("У пользователя уже есть активная аренда!");
        }
        User user = userDao.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
        Scooter scooter = scooterDao.findById(scooterId).orElseThrow(() -> new ResourceNotFoundException("Самокат не найден"));
        Tariff tariff = tariffDao.findById(tariffId).orElseThrow(() -> new ResourceNotFoundException("Тари не найден"));
        if (scooter.getStatus() != Status.AVAILABLE) {
            throw new BusinessLogicException("Самокат недоступен для аренды! Текущий статус: " + scooter.getStatus());
        }
        scooter.setStatus(Status.RENTED);
        Rental rental = new Rental(user,scooter,tariff, LocalDateTime.now(),scooter.getRentalPoint());
        scooter.setRentalPoint(null);
        rentalDao.save(rental);
        return rental;
    }

    public Rental finishRental(Long userId, Long endPointId, BigDecimal distance){
        Rental rental = rentalDao.findActiveRentalByUserId(userId).orElseThrow(()-> new BusinessLogicException("У пользователя нет активных аренд"));
        RentalPoint endPoint = rentalPointDao.findById(endPointId).orElseThrow(() -> new ResourceNotFoundException("Точка завершения не найдена"));
        rental.setEndTime(LocalDateTime.now());
        rental.setEndPoint(endPoint);
        BigDecimal totalCost = calculateCost(rental.getStartTime(),rental.getEndTime(),rental.getTariff());

        Scooter scooter = rental.getScooter();
        scooter.setRentalPoint(endPoint);
        scooter.setStatus(Status.AVAILABLE);
        scooter.setMileage(scooter.getMileage().add(distance));
        return rental;
    }
    private BigDecimal calculateCost(LocalDateTime start, LocalDateTime end, Tariff tariff) {
        // Считаем продолжительность в минутах
        long minutes = Duration.between(start, end).toMinutes();

        // Переводим минуты в часы
        BigDecimal hours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        // Если это почасовой тариф: (Цена * Часы) - Скидка
        if ("HOURLY".equalsIgnoreCase(tariff.getType())) {
            BigDecimal cost = tariff.getPrice().multiply(hours).subtract(tariff.getDiscount());
            return cost.compareTo(BigDecimal.ZERO) > 0 ? cost : BigDecimal.ZERO;// Стоимость не может быть отрицательной (если скидка больше цены)
        }
        // Если это абонемент (FIXED), просто берем цену минус скидка
        else {
            BigDecimal cost = tariff.getPrice().subtract(tariff.getDiscount());
            return cost.compareTo(BigDecimal.ZERO) > 0 ? cost : BigDecimal.ZERO;
        }
    }
    @Transactional(readOnly = true)
    public List<Rental> getUserHistory(Long userId){
        return rentalDao.findHistoryByUserId(userId);
    }
    @Transactional(readOnly = true)
    public List<Rental> getScooterHistory(Long scooterId) {
        return rentalDao.findHistoryByScooterId(scooterId);
    }

}
