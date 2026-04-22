package com.example.scooterrentalsystem.dao;

import com.example.scooterrentalsystem.entity.RentalPoint;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RentalPointDao {
    @PersistenceContext
    private EntityManager em;

    public void save(RentalPoint rentalPoint) {
        em.persist(rentalPoint);
        em.flush();
    }

    public void delete(RentalPoint rentalPoint) {
        em.remove(em.contains(rentalPoint) ? rentalPoint : em.merge(rentalPoint));
    }

    public Optional<RentalPoint> findById(Long id) {
        RentalPoint rentalPoint = em.find(RentalPoint.class, id);
        return Optional.ofNullable(rentalPoint);
    }

    public List<RentalPoint> findByParentId(Long parentId) {
        return em.createQuery(
                        "SELECT r FROM RentalPoint r WHERE r.parent.id = :parent ORDER BY r.name",
                        RentalPoint.class)
                .setParameter("parent", parentId)
                .getResultList();
    }

    public List<RentalPoint> findRootPoints() {
        return em.createQuery("select r from RentalPoint r where r.parent is null", RentalPoint.class).getResultList();
    }

    public List<RentalPoint> findAll() {
        return em.createQuery("SELECT r FROM RentalPoint r ORDER BY r.id", RentalPoint.class).getResultList();
    }

    public long countChildren(Long parentId) {
        Long c = em.createQuery(
                        "SELECT COUNT(r) FROM RentalPoint r WHERE r.parent.id = :pid",
                        Long.class)
                .setParameter("pid", parentId)
                .getSingleResult();
        return c != null ? c : 0L;
    }
}
