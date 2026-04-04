package com.example.scooterrentalsystem.dao;

import com.example.scooterrentalsystem.entity.RentalPoint;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

public class RentalPointDao {
    @PersistenceContext
    private EntityManager em;
    public void save(RentalPoint rentalPoint){
        em.persist(rentalPoint);
    }
    public Optional<RentalPoint> findById(Long id ){
        RentalPoint rentalPoint = em.find(RentalPoint.class,id);
        return Optional.ofNullable(rentalPoint);
    }
    public List<RentalPoint> findByParentId(Long parentId){
        return em.createQuery("SELECT r FROM RentalPoint r where r.parent.id=:parent", RentalPoint.class).setParameter("parent",parentId).getResultList();
    }
    public List<RentalPoint> findRootPoints(){
        return em.createQuery("select r from RentalPoint r where r.parent is null", RentalPoint.class).getResultList();
    }
}
