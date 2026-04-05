package com.example.scooterrentalsystem.dao;

import com.example.scooterrentalsystem.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleDao {
    @PersistenceContext
    EntityManager em;
    public Optional<Role> findByName(String name){
        return em.createQuery("SELECT r FROM Role r where r.name=:name", Role.class).setParameter("name",name).getResultStream().findFirst();
    }
}
