package com.example.scooterrentalsystem.dao;

import com.example.scooterrentalsystem.entity.Scooter;
import com.example.scooterrentalsystem.entity.Status;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class ScooterDao {
    @PersistenceContext
    private EntityManager entityManager;

    public void save(Scooter scooter) { entityManager.persist(scooter); }

    public Optional<Scooter> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Scooter.class, id));
    }
    public List<Scooter> findAvailableByRentalPoint(Long pointId) {
        return entityManager.createQuery(
                        "SELECT s FROM Scooter s WHERE s.rentalPoint.id = :pointId AND s.status =:status", Scooter.class)
                .setParameter("pointId", pointId).setParameter("status", Status.AVAILABLE)
                .getResultList();
    }
}
