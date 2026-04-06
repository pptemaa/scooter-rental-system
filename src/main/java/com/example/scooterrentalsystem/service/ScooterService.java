package com.example.scooterrentalsystem.service;

import com.example.scooterrentalsystem.dao.ScooterDao;
import com.example.scooterrentalsystem.entity.RentalPoint;
import com.example.scooterrentalsystem.entity.Scooter;
import com.example.scooterrentalsystem.entity.ScooterModel;
import com.example.scooterrentalsystem.entity.Status;
import com.example.scooterrentalsystem.exeption.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ScooterService {
    private final ScooterDao scooterDao;

    public ScooterService(ScooterDao scooterDao) {
        this.scooterDao = scooterDao;
    }

    public Scooter addScooter(ScooterModel scooterModel, RentalPoint rentalPoint) {
        Scooter scooter = new Scooter(scooterModel, rentalPoint, Status.AVAILABLE, BigDecimal.ZERO);
        scooterDao.save(scooter);
        return scooter;
    }


    public void updateScooterStatus(Long scooterId, Status newStatus) {
        Scooter scooter = scooterDao.findById(scooterId).orElseThrow(() -> new ResourceNotFoundException("Самокат с ID " + scooterId + " не найден"));
        scooter.setStatus(newStatus);
    }

    public void addMileage(Long scooterId, BigDecimal distance) {
        Scooter scooter = scooterDao.findById(scooterId).orElseThrow(() -> new ResourceNotFoundException("Самокат с ID " + scooterId + " не найден"));
        BigDecimal oldDistance = scooter.getMileage();
        scooter.setMileage(scooter.getMileage().add(distance));
    }

    @Transactional(readOnly = true)
    public List<Scooter> getAvailableScooters(Long pointId) {
        return scooterDao.findAvailableByRentalPoint(pointId);
    }
}

