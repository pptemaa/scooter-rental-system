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
    public Tariff save(Tariff tariff){
        if (tariff.getId()==null){
            entityManager.persist(tariff);
            return tariff;
        }else{
            return entityManager.merge(tariff);
        }
    }

    public void delete(Tariff tariff) {
        entityManager.remove(entityManager.contains(tariff) ? tariff : entityManager.merge(tariff));
    }
}
