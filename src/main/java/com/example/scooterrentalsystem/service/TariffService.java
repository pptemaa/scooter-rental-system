package com.example.scooterrentalsystem.service;

import com.example.scooterrentalsystem.dao.TariffDao;
import com.example.scooterrentalsystem.entity.Tariff;
import com.example.scooterrentalsystem.exeption.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TariffService {
    private final TariffDao tariffDao;

    public TariffService(TariffDao tariffDao) {
        this.tariffDao = tariffDao;
    }
    @Transactional(readOnly = true)
    public List<Tariff> getAllTariffs(){
        return tariffDao.findAll();
    }
    @Transactional(readOnly = true)
    public Tariff getTariffById(Long id) {
        return tariffDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Тариф с ID " + id + " не найден"));
    }

}
