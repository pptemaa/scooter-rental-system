package com.example.scooterrentalsystem.dao;

import com.example.scooterrentalsystem.entity.Tariff;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class TariffDao {
    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Tariff> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Tariff.class, id));
    }

    public List<Tariff> findAll() {
        return entityManager.createQuery("SELECT t FROM Tariff t", Tariff.class).getResultList();
    }
}
