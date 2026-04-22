package com.example.scooterrentalsystem.service;

import com.example.scooterrentalsystem.dao.RentalDao;
import com.example.scooterrentalsystem.dao.TariffDao;
import com.example.scooterrentalsystem.entity.Tariff;
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
public class TariffService {

    private static final Logger log = LoggerFactory.getLogger(TariffService.class);

    private final TariffDao tariffDao;
    private final RentalDao rentalDao;

    public TariffService(TariffDao tariffDao, RentalDao rentalDao) {
        this.tariffDao = tariffDao;
        this.rentalDao = rentalDao;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "tariffs")
    public List<Tariff> getAllTariffs() {
        return tariffDao.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "tariffs", key = "#id")
    public Tariff getTariffById(Long id) {
        return tariffDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Тариф с ID " + id + " не найден"));
    }
    @CacheEvict(value = "tariffs",allEntries = true)
    public Tariff createTariff(Tariff tariff) {
        Tariff saved = tariffDao.save(tariff);
        log.info("Создан тариф id={}", saved.getId());
        return saved;
    }
    @CacheEvict(value = "tariffs",allEntries = true)
    public Tariff updateTariff(Long id, Tariff patch) {
        Tariff existing = getTariffById(id);
        if (patch.getName() != null) {
            existing.setName(patch.getName());
        }
        if (patch.getType() != null) {
            existing.setType(patch.getType());
        }
        if (patch.getPrice() != null) {
            existing.setPrice(patch.getPrice());
        }
        if (patch.getDiscount() != null) {
            existing.setDiscount(patch.getDiscount());
        }
        Tariff saved = tariffDao.save(existing);
        log.info("Обновлён тариф id={}", id);
        return saved;
    }
    @CacheEvict(value = "tariffs",allEntries = true)
    public void deleteTariff(Long id) {
        Tariff tariff = getTariffById(id);
        if (rentalDao.countByTariffId(id) > 0) {
            throw new ConflictException("Нельзя удалить тариф: он используется в арендах");
        }
        tariffDao.delete(tariff);
        log.info("Удалён тариф id={}", id);
    }
}
