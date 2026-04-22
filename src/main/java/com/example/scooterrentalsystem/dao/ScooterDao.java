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

    public void save(Scooter scooter) {
        entityManager.persist(scooter);
        entityManager.flush();
    }

    public Optional<Scooter> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Scooter.class, id));
    }

    /**
     * Загружает связанные модель и точку для маппинга в DTO.
     */
    public List<Scooter> findAllWithAssociations() {
        return entityManager.createQuery(
                        "SELECT DISTINCT s FROM Scooter s "
                                + "LEFT JOIN FETCH s.scooterModel "
                                + "LEFT JOIN FETCH s.rentalPoint "
                                + "ORDER BY s.id",
                        Scooter.class)
                .getResultList();
    }

    public Optional<Scooter> findByIdWithAssociations(Long id) {
        List<Scooter> list = entityManager.createQuery(
                        "SELECT DISTINCT s FROM Scooter s "
                                + "LEFT JOIN FETCH s.scooterModel "
                                + "LEFT JOIN FETCH s.rentalPoint "
                                + "WHERE s.id = :id",
                        Scooter.class)
                .setParameter("id", id)
                .setMaxResults(1)
                .getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public List<Scooter> findAvailableByRentalPoint(Long pointId) {
        return entityManager.createQuery(
                        "SELECT s FROM Scooter s WHERE s.rentalPoint.id = :pointId AND s.status = :status",
                        Scooter.class)
                .setParameter("pointId", pointId)
                .setParameter("status", Status.AVAILABLE)
                .getResultList();
    }

    public long countByModelId(Long modelId) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(s) FROM Scooter s WHERE s.scooterModel.id = :mid",
                        Long.class)
                .setParameter("mid", modelId)
                .getSingleResult();
        return count != null ? count : 0L;
    }

    public long countByRentalPointId(Long rentalPointId) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(s) FROM Scooter s WHERE s.rentalPoint.id = :pid",
                        Long.class)
                .setParameter("pid", rentalPointId)
                .getSingleResult();
        return count != null ? count : 0L;
    }

    public void delete(Scooter scooter) {
        entityManager.remove(entityManager.contains(scooter) ? scooter : entityManager.merge(scooter));
    }
}
