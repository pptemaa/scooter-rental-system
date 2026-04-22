package com.example.scooterrentalsystem.dao;

import com.example.scooterrentalsystem.entity.ScooterModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ScooterModelDao {

    @PersistenceContext
    private EntityManager em;

    public void save(ScooterModel model) {
        em.persist(model);
        em.flush();
    }

    public Optional<ScooterModel> findById(Long id) {
        return Optional.ofNullable(em.find(ScooterModel.class, id));
    }

    public List<ScooterModel> findAll() {
        return em.createQuery("SELECT m FROM ScooterModel m ORDER BY m.name", ScooterModel.class).getResultList();
    }

    public ScooterModel merge(ScooterModel model) {
        return em.merge(model);
    }

    public void delete(ScooterModel model) {
        em.remove(em.contains(model) ? model : em.merge(model));
    }
}
