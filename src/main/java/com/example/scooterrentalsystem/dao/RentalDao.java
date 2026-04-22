package com.example.scooterrentalsystem.dao;

import com.example.scooterrentalsystem.entity.Rental;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RentalDao {
    @PersistenceContext
    EntityManager em;

    public void save(Rental rental) {
        em.persist(rental);
        em.flush();
    }

    public Rental merge(Rental rental) {
        return em.merge(rental);
    }

    public Optional<Rental> findById(Long id) {
        return Optional.ofNullable(em.find(Rental.class, id));
    }

    public Optional<Rental> findByIdWithAssociations(Long id) {
        List<Rental> list = em.createQuery(
                        "SELECT DISTINCT r FROM Rental r "
                                + "LEFT JOIN FETCH r.user u LEFT JOIN FETCH u.role "
                                + "LEFT JOIN FETCH r.scooter s LEFT JOIN FETCH s.scooterModel "
                                + "LEFT JOIN FETCH r.tariff "
                                + "LEFT JOIN FETCH r.startPoint LEFT JOIN FETCH r.endPoint "
                                + "WHERE r.id = :id",
                        Rental.class)
                .setParameter("id", id)
                .setMaxResults(1)
                .getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public Optional<Rental> findActiveRentalByUserId(Long userId) {
        return em.createQuery(
                        "SELECT r FROM Rental r WHERE r.endTime IS NULL AND r.user.id = :userId",
                        Rental.class)
                .setParameter("userId", userId)
                .getResultStream()
                .findFirst();
    }

    public Optional<Rental> findActiveRentalByScooterId(Long scooterId) {
        return em.createQuery(
                        "SELECT r FROM Rental r WHERE r.endTime IS NULL AND r.scooter.id = :sid",
                        Rental.class)
                .setParameter("sid", scooterId)
                .getResultStream()
                .findFirst();
    }

    public List<Rental> findHistoryByUserId(Long userId) {
        return em.createQuery(
                        "SELECT DISTINCT r FROM Rental r "
                                + "LEFT JOIN FETCH r.scooter s LEFT JOIN FETCH s.scooterModel "
                                + "LEFT JOIN FETCH r.tariff "
                                + "LEFT JOIN FETCH r.startPoint LEFT JOIN FETCH r.endPoint "
                                + "WHERE r.user.id = :userId AND r.endTime IS NOT NULL "
                                + "ORDER BY r.startTime DESC",
                        Rental.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<Rental> findHistoryByScooterId(Long scooterId) {
        return em.createQuery(
                        "SELECT DISTINCT r FROM Rental r "
                                + "LEFT JOIN FETCH r.user u LEFT JOIN FETCH u.role "
                                + "LEFT JOIN FETCH r.tariff "
                                + "LEFT JOIN FETCH r.startPoint LEFT JOIN FETCH r.endPoint "
                                + "WHERE r.scooter.id = :scooterId AND r.endTime IS NOT NULL "
                                + "ORDER BY r.startTime DESC",
                        Rental.class)
                .setParameter("scooterId", scooterId)
                .getResultList();
    }

    public long countByTariffId(Long tariffId) {
        Long c = em.createQuery(
                        "SELECT COUNT(r) FROM Rental r WHERE r.tariff.id = :tid",
                        Long.class)
                .setParameter("tid", tariffId)
                .getSingleResult();
        return c != null ? c : 0L;
    }

    public List<Rental> findAllWithAssociations() {
        return em.createQuery(
                        "SELECT DISTINCT r FROM Rental r "
                                + "LEFT JOIN FETCH r.user u LEFT JOIN FETCH u.role "
                                + "LEFT JOIN FETCH r.scooter s LEFT JOIN FETCH s.scooterModel "
                                + "LEFT JOIN FETCH r.tariff "
                                + "LEFT JOIN FETCH r.startPoint LEFT JOIN FETCH r.endPoint "
                                + "ORDER BY r.startTime DESC",
                        Rental.class)
                .getResultList();
    }
}
