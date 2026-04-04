package com.example.scooterrentalsystem.dao;

import com.example.scooterrentalsystem.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {
    @PersistenceContext
    private EntityManager em;
    public void save(User user){
        em.persist(user);
    }
    public Optional<User> findByEmail(String email){
        return em.createQuery("SELECT u FROM User u where u.email = :email", User.class).setParameter("email",email).getResultStream().findFirst();
    }
    public Optional<User> findById(Long id){
        User user = em.find(User.class,id);
        return Optional.ofNullable(user);
    }
}
