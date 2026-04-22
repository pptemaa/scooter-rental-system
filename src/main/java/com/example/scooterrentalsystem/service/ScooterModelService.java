package com.example.scooterrentalsystem.service;

import com.example.scooterrentalsystem.dao.ScooterDao;
import com.example.scooterrentalsystem.dao.ScooterModelDao;
import com.example.scooterrentalsystem.entity.ScooterModel;
import com.example.scooterrentalsystem.exeption.ConflictException;
import com.example.scooterrentalsystem.exeption.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ScooterModelService {

    private static final Logger log = LoggerFactory.getLogger(ScooterModelService.class);

    private final ScooterModelDao scooterModelDao;
    private final ScooterDao scooterDao;

    public ScooterModelService(ScooterModelDao scooterModelDao, ScooterDao scooterDao) {
        this.scooterModelDao = scooterModelDao;
        this.scooterDao = scooterDao;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "models")
    public List<ScooterModel> getAllModels() {
        return scooterModelDao.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "models", key = "#id")
    public ScooterModel getById(Long id) {
        return scooterModelDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Модель самоката с ID " + id + " не найдена"));
    }
    @CacheEvict(value = "models", allEntries = true)
    public ScooterModel create(ScooterModel model) {
        scooterModelDao.save(model);
        log.info("Создана модель самоката id={}", model.getId());
        return model;
    }
    @CacheEvict(value = "models", allEntries = true)
    public ScooterModel update(Long id, String name, Integer maxWeight) {
        ScooterModel model = getById(id);
        if (name != null) {
            model.setName(name);
        }
        if (maxWeight != null) {
            model.setMax_weight(maxWeight);
        }
        ScooterModel merged = scooterModelDao.merge(model);
        log.info("Обновлена модель самоката id={}", id);
        return merged;
    }
    @CacheEvict(value = "models", allEntries = true)
    public void delete(Long id) {
        ScooterModel model = getById(id);
        if (scooterDao.countByModelId(id) > 0) {
            throw new ConflictException("Нельзя удалить модель: есть самокаты этой модели");
        }
        scooterModelDao.delete(model);
        log.info("Удалена модель самоката id={}", id);
    }
}
