package com.example.scooterrentalsystem.service;

import com.example.scooterrentalsystem.dao.*;
import com.example.scooterrentalsystem.entity.*;
import com.example.scooterrentalsystem.exeption.BusinessLogicException;
import com.example.scooterrentalsystem.exeption.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(RentalService.class);

    private final RentalDao rentalDao;
    private final RentalPointDao rentalPointDao;
    private final ScooterDao scooterDao;
    private final TariffDao tariffDao;
    private final UserDao userDao;

    public RentalService(
            RentalDao rentalDao,
            RentalPointDao rentalPointDao,
            ScooterDao scooterDao,
            TariffDao tariffDao,
            UserDao userDao
    ) {
        this.rentalDao = rentalDao;
        this.rentalPointDao = rentalPointDao;
        this.scooterDao = scooterDao;
        this.tariffDao = tariffDao;
        this.userDao = userDao;
    }

    public Rental startRental(Long userId, Long scooterId, Long tariffId) {
        if (rentalDao.findActiveRentalByUserId(userId).isPresent()) {
            throw new BusinessLogicException("У пользователя уже есть активная аренда");
        }
        if (rentalDao.findActiveRentalByScooterId(scooterId).isPresent()) {
            throw new BusinessLogicException("Этот самокат уже находится в аренде");
        }

        User user = userDao.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
        Scooter scooter = scooterDao.findById(scooterId).orElseThrow(() -> new ResourceNotFoundException("Самокат не найден"));
        Tariff tariff = tariffDao.findById(tariffId).orElseThrow(() -> new ResourceNotFoundException("Тариф не найден"));

        if (scooter.getStatus() != Status.AVAILABLE) {
            throw new BusinessLogicException("Самокат недоступен для аренды. Текущий статус: " + scooter.getStatus());
        }
        if (scooter.getRentalPoint() == null) {
            throw new BusinessLogicException("У самоката не привязана точка начала аренды (rental_point отсутствует)");
        }

        scooter.setStatus(Status.RENTED);
        Rental rental =
                new Rental(user, scooter, tariff, LocalDateTime.now(), scooter.getRentalPoint());
        scooter.setRentalPoint(null);
        rentalDao.save(rental);

        log.info(
                "Начата аренда id={} user={} scooter={} tariff={}",
                rental.getId(),
                userId,
                scooterId,
                tariffId
        );
        return rentalDao.findByIdWithAssociations(rental.getId()).orElse(rental);
    }

    public Rental finishRental(Long userId, Long endPointId, BigDecimal distanceKm) {
        Rental rental =
                rentalDao.findActiveRentalByUserId(userId).orElseThrow(
                        () -> new BusinessLogicException("У пользователя нет активных аренд"));

        RentalPoint endPoint =
                rentalPointDao.findById(endPointId).orElseThrow(
                        () -> new ResourceNotFoundException("Точка завершения не найдена"));

        rental.setEndTime(LocalDateTime.now());
        rental.setEndPoint(endPoint);

        BigDecimal totalCost =
                calculateCost(rental.getStartTime(), rental.getEndTime(), rental.getTariff());
        rental.setTotalCost(totalCost);

        Scooter scooter = rental.getScooter();
        scooter.setRentalPoint(endPoint);
        scooter.setStatus(Status.AVAILABLE);
        scooter.setMileage(scooter.getMileage().add(distanceKm));

        Rental merged = rentalDao.merge(rental);
        log.info(
                "Завершена аренда id={} user={} totalCost={}",
                merged.getId(),
                userId,
                totalCost
        );
        return rentalDao.findByIdWithAssociations(merged.getId()).orElse(merged);
    }

    private BigDecimal calculateCost(LocalDateTime start, LocalDateTime end, Tariff tariff) {
        long minutes = Duration.between(start, end).toMinutes();
        BigDecimal hours =
                BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 4, RoundingMode.HALF_UP);

        String type = tariff.getType() != null ? tariff.getType().trim() : "";
        BigDecimal discount = tariff.getDiscount() != null ? tariff.getDiscount() : BigDecimal.ZERO;

        if ("HOURLY".equalsIgnoreCase(type)) {
            BigDecimal cost = tariff.getPrice().multiply(hours).subtract(discount);
            return cost.compareTo(BigDecimal.ZERO) > 0 ? cost.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal cost = tariff.getPrice().subtract(discount);
        return cost.compareTo(BigDecimal.ZERO) > 0 ? cost.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    @Transactional(readOnly = true)
    public List<Rental> getUserHistory(Long userId) {
        userDao.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
        return rentalDao.findHistoryByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Rental> getScooterHistory(Long scooterId) {
        scooterDao.findById(scooterId).orElseThrow(() -> new ResourceNotFoundException("Самокат не найден"));
        return rentalDao.findHistoryByScooterId(scooterId);
    }

    @Transactional(readOnly = true)
    public List<Rental> getAllRentals() {
        return rentalDao.findAllWithAssociations();
    }

    @Transactional(readOnly = true)
    public Rental getRentalById(Long id) {
        return rentalDao.findByIdWithAssociations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Аренда с ID " + id + " не найдена"));
    }
}
