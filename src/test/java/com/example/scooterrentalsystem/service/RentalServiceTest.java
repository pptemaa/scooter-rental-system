package com.example.scooterrentalsystem.service;

import com.example.scooterrentalsystem.dao.*;
import com.example.scooterrentalsystem.entity.*;
import com.example.scooterrentalsystem.exeption.BusinessLogicException;
import com.example.scooterrentalsystem.exeption.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock private RentalDao rentalDao;
    @Mock private RentalPointDao rentalPointDao;
    @Mock private ScooterDao scooterDao;
    @Mock private TariffDao tariffDao;
    @Mock private UserDao userDao;

    @InjectMocks
    private RentalService rentalService;

    @Test
    @DisplayName("Should start rental successfully")
    void startRental_Success() {
        Long userId = 1L;
        Long scooterId = 2L;
        Long tariffId = 3L;

        User user = new User();
        user.setId(userId);
        user.setBalance(BigDecimal.valueOf(100));

        Scooter scooter = new Scooter();
        scooter.setId(scooterId);
        scooter.setStatus(Status.AVAILABLE);
        scooter.setRentalPoint(new RentalPoint());

        Tariff tariff = new Tariff();
        tariff.setId(tariffId);

        when(userDao.findById(userId)).thenReturn(Optional.of(user));
        when(scooterDao.findById(scooterId)).thenReturn(Optional.of(scooter));
        when(tariffDao.findById(tariffId)).thenReturn(Optional.of(tariff));
        when(rentalDao.findActiveRentalByUserId(userId)).thenReturn(Optional.empty());
        when(rentalDao.findActiveRentalByScooterId(scooterId)).thenReturn(Optional.empty());

        Rental rental = new Rental(user, scooter, tariff, LocalDateTime.now(), scooter.getRentalPoint());
        when(rentalDao.findByIdWithAssociations(any())).thenReturn(Optional.of(rental));

        Rental result = rentalService.startRental(userId, scooterId, tariffId);

        assertNotNull(result);
        assertEquals(Status.RENTED, scooter.getStatus());
        verify(rentalDao).save(any(Rental.class));
    }

    @Test
    @DisplayName("Should throw exception if balance is zero or negative")
    void startRental_InsufficientBalance() {
        User user = new User();
        user.setBalance(BigDecimal.ZERO);
        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BusinessLogicException.class, () -> rentalService.startRental(1L, 2L, 3L));
    }

    @Test
    @DisplayName("Should throw exception if scooter is not available")
    void startRental_ScooterNotAvailable() {
        User user = new User();
        user.setBalance(BigDecimal.TEN);
        Scooter scooter = new Scooter();
        scooter.setStatus(Status.MAINTENANCE);

        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        when(scooterDao.findById(2L)).thenReturn(Optional.of(scooter));
        when(tariffDao.findById(3L)).thenReturn(Optional.of(new Tariff()));

        assertThrows(BusinessLogicException.class, () -> rentalService.startRental(1L, 2L, 3L));
    }

    @Test
    @DisplayName("Should finish rental and calculate cost")
    void finishRental_Success() {
        Long userId = 1L;
        Long endPointId = 10L;
        BigDecimal distance = BigDecimal.valueOf(5);

        User user = new User();
        user.setBalance(BigDecimal.valueOf(100));

        Scooter scooter = new Scooter();
        scooter.setMileage(BigDecimal.ZERO);

        Tariff tariff = new Tariff();
        tariff.setType(TariffType.MINUTE);
        tariff.setPrice(BigDecimal.valueOf(2)); // 2 per minute

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setScooter(scooter);
        rental.setTariff(tariff);
        rental.setStartTime(LocalDateTime.now().minusMinutes(10));

        RentalPoint endPoint = new RentalPoint();

        when(rentalDao.findActiveRentalByUserId(userId)).thenReturn(Optional.of(rental));
        when(rentalPointDao.findById(endPointId)).thenReturn(Optional.of(endPoint));
        when(rentalDao.merge(any())).thenReturn(rental);
        when(rentalDao.findByIdWithAssociations(any())).thenReturn(Optional.of(rental));

        Rental result = rentalService.finishRental(userId, endPointId, distance);

        assertNotNull(result);
        assertEquals(Status.AVAILABLE, scooter.getStatus());
        assertEquals(0, BigDecimal.valueOf(5).compareTo(scooter.getMileage()));
        // 10 minutes * 2 = 20 cost. Balance 100 - 20 = 80.
        assertEquals(0, BigDecimal.valueOf(80).compareTo(user.getBalance()));
        verify(rentalDao).merge(rental);
    }
}
