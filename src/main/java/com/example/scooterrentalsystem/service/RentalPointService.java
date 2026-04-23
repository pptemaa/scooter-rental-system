package com.example.scooterrentalsystem.service;

import com.example.scooterrentalsystem.dao.RentalPointDao;
import com.example.scooterrentalsystem.dao.ScooterDao;
import com.example.scooterrentalsystem.entity.RentalPoint;
import com.example.scooterrentalsystem.entity.Scooter;
import com.example.scooterrentalsystem.exeption.ConflictException;
import com.example.scooterrentalsystem.exeption.ResourceNotFoundException;
import com.example.scooterrentalsystem.validation.CoordinateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class RentalPointService {

    private static final Logger log = LoggerFactory.getLogger(RentalPointService.class);

    private final RentalPointDao rentalPointDao;
    private final ScooterDao scooterDao;

    public RentalPointService(RentalPointDao rentalPointDao, ScooterDao scooterDao) {
        this.rentalPointDao = rentalPointDao;
        this.scooterDao = scooterDao;
    }

    public RentalPoint addRentalPoint(String name, BigDecimal latitude, BigDecimal longitude, Long parentId) {
        CoordinateValidator.requireValidCoordinates(latitude, longitude);
        RentalPoint parent = null;
        if (parentId != null) {
            parent = rentalPointDao.findById(parentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Родительская точка с ID " + parentId + " не найдена"));
        }
        RentalPoint newPoint = new RentalPoint(name, latitude, longitude, parent);
        rentalPointDao.save(newPoint);
        log.info("Создана точка проката id={} parentId={}", newPoint.getId(), parentId);
        return newPoint;
    }

    public void updateRentalPoint(Long id, String name, BigDecimal latitude, BigDecimal longitude, Long parentId) {
        CoordinateValidator.requireValidCoordinates(latitude, longitude);
        RentalPoint point = rentalPointDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Точка проката не найдена"));
        point.setName(name);
        point.setLatitude(latitude);
        point.setLongitude(longitude);
        if (parentId != null) {
            if (parentId.equals(id)) {
                throw new ConflictException("Точка не может быть родителем самой себе");
            }
            RentalPoint parent = rentalPointDao.findById(parentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Родительская точка с ID " + parentId + " не найдена"));
            point.setParent(parent);
        }
        log.info("Обновлена точка проката id={}", id);
    }

    public void deleteRentalPoint(Long id) {
        RentalPoint point = rentalPointDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Точка проката не найдена"));
        long scootersHere = scooterDao.countByRentalPointId(id);
        if (scootersHere > 0) {
            throw new ConflictException("Невозможно удалить точку проката: на ней ещё есть самокаты");
        }
        long children = rentalPointDao.countChildren(id);
        if (children > 0) {
            throw new ConflictException("Невозможно удалить точку проката: есть дочерние точки");
        }
        rentalPointDao.delete(point);
        log.info("Удалена точка проката id={}", id);
    }

    @Transactional(readOnly = true)
    public RentalPoint getById(Long id) {
        return rentalPointDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Точка проката не найдена"));
    }

    @Transactional(readOnly = true)
    public List<RentalPoint> getAllPoints() {
        return rentalPointDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<RentalPoint> getRootPoints() {
        return rentalPointDao.findRootPoints();
    }

    @Transactional(readOnly = true)
    public List<RentalPoint> getSubPoints(Long parentId) {
        rentalPointDao.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Родительская точка с ID " + parentId + " не найдена"));
        return rentalPointDao.findByParentId(parentId);
    }

    @Transactional(readOnly = true)
    public List<Scooter> getScootersAtPoint(Long pointId) {
        return scooterDao.findByRentalPointId(pointId);
    }

    @Transactional(readOnly = true)
    public List<Scooter> getAvailableScootersAtPoint(Long pointId) {
        rentalPointDao.findById(pointId)
                .orElseThrow(() -> new ResourceNotFoundException("Точка проката не найдена"));
        return scooterDao.findAvailableByRentalPoint(pointId);
    }

}
