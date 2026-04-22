package com.example.scooterrentalsystem.service;

import com.example.scooterrentalsystem.dao.RentalDao;
import com.example.scooterrentalsystem.dao.RentalPointDao;
import com.example.scooterrentalsystem.dao.ScooterDao;
import com.example.scooterrentalsystem.dao.ScooterModelDao;
import com.example.scooterrentalsystem.dto.ScooterUpdateDto;
import com.example.scooterrentalsystem.entity.RentalPoint;
import com.example.scooterrentalsystem.entity.Scooter;
import com.example.scooterrentalsystem.entity.ScooterModel;
import com.example.scooterrentalsystem.entity.Status;
import com.example.scooterrentalsystem.exeption.BusinessLogicException;
import com.example.scooterrentalsystem.exeption.ConflictException;
import com.example.scooterrentalsystem.exeption.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ScooterService {

    private static final Logger log = LoggerFactory.getLogger(ScooterService.class);

    private final ScooterDao scooterDao;
    private final ScooterModelDao scooterModelDao;
    private final RentalPointDao rentalPointDao;
    private final RentalDao rentalDao;

    public ScooterService(
            ScooterDao scooterDao,
            ScooterModelDao scooterModelDao,
            RentalPointDao rentalPointDao,
            RentalDao rentalDao
    ) {
        this.scooterDao = scooterDao;
        this.scooterModelDao = scooterModelDao;
        this.rentalPointDao = rentalPointDao;
        this.rentalDao = rentalDao;
    }

    @Transactional(readOnly = true)
    public List<Scooter> getAllScooters() {
        return scooterDao.findAllWithAssociations();
    }

    @Transactional(readOnly = true)
    public Scooter getScooterById(Long id) {
        return scooterDao.findByIdWithAssociations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Самокат с ID " + id + " не найден"));
    }

    public Scooter addScooter(Long modelId, Long rentalPointId) {
        ScooterModel model = scooterModelDao.findById(modelId)
                .orElseThrow(() -> new ResourceNotFoundException("Модель самоката с ID " + modelId + " не найдена"));
        RentalPoint point = null;
        if (rentalPointId != null) {
            point = rentalPointDao.findById(rentalPointId)
                    .orElseThrow(() -> new ResourceNotFoundException("Точка проката с ID " + rentalPointId + " не найдена"));
        }
        Scooter scooter = new Scooter(model, point, Status.AVAILABLE, BigDecimal.ZERO);
        scooterDao.save(scooter);
        log.info("Создан самокат id={} modelId={} pointId={}", scooter.getId(), modelId, rentalPointId);
        return scooterDao.findByIdWithAssociations(scooter.getId()).orElse(scooter);
    }

    public void updateScooter(Long id, ScooterUpdateDto dto) {
        Scooter scooter = scooterDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Самокат с ID " + id + " не найден"));

        if (dto.modelId() != null) {
            ScooterModel model = scooterModelDao.findById(dto.modelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Модель самоката с ID " + dto.modelId() + " не найдена"));
            scooter.setScooterModel(model);
        }
        if (dto.rentalPointId() != null) {
            RentalPoint point = rentalPointDao.findById(dto.rentalPointId())
                    .orElseThrow(() -> new ResourceNotFoundException("Точка проката с ID " + dto.rentalPointId() + " не найдена"));
            scooter.setRentalPoint(point);
        }
        if (dto.status() != null) {
            scooter.setStatus(dto.status());
        }
        log.info("Обновлён самокат id={}", id);
    }

    public void updateScooterStatus(Long scooterId, Status newStatus) {
        Scooter scooter = scooterDao.findById(scooterId)
                .orElseThrow(() -> new ResourceNotFoundException("Самокат с ID " + scooterId + " не найден"));
        scooter.setStatus(newStatus);
        log.info("Статус самоката id={} -> {}", scooterId, newStatus);
    }

    public void addMileage(Long scooterId, BigDecimal distance) {
        Scooter scooter = scooterDao.findById(scooterId)
                .orElseThrow(() -> new ResourceNotFoundException("Самокат с ID " + scooterId + " не найден"));
        scooter.setMileage(scooter.getMileage().add(distance));
        log.info("Пробег самоката id={} увеличен на {}", scooterId, distance);
    }

    public void deleteScooter(Long scooterId) {
        Scooter scooter = scooterDao.findById(scooterId)
                .orElseThrow(() -> new ResourceNotFoundException("Самокат с ID " + scooterId + " не найден"));
        if (rentalDao.findActiveRentalByScooterId(scooterId).isPresent()) {
            throw new ConflictException("Нельзя удалить самокат с активной арендой");
        }
        scooterDao.delete(scooter);
        log.info("Удалён самокат id={}", scooterId);
    }

    @Transactional(readOnly = true)
    public List<Scooter> getAvailableScooters(Long pointId) {
        return scooterDao.findAvailableByRentalPoint(pointId);
    }
}
