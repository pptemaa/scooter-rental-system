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
    public void save(Rental rental){
        em.persist(rental);
    }
    public Optional<Rental> findById(Long id){
        Rental rental = em.find(Rental.class,id);
        return Optional.ofNullable(rental);
    }
    public Optional<Rental> findActiveRentalByUserId(Long userId){
        return em.createQuery("SELECT r FROM Rental r where r.endTime is NULL and r.user.id=:userId", Rental.class).setParameter("userId",userId).getResultStream().findFirst();
    }
    public List<Rental> findHistoryByUserId(Long userId){
        return em.createQuery("SELECT r FROM Rental r WHERE r.user.id=:userId and r.endTime is not null order by r.startTime desc", Rental.class).setParameter("userId",userId).getResultList();
    }
}
