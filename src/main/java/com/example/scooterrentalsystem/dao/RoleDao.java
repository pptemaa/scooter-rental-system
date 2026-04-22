package com.example.scooterrentalsystem.dao;

import com.example.scooterrentalsystem.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoleDao {
    @PersistenceContext
    EntityManager em;

    public Optional<Role> findByName(String name) {
        return em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst();
    }

    public Optional<Role> findById(Long id) {
        return Optional.ofNullable(em.find(Role.class, id));
    }

    public List<Role> findAll() {
        return em.createQuery("SELECT r FROM Role r ORDER BY r.name", Role.class).getResultList();
    }

    public void save(Role role) {
        em.persist(role);
        em.flush();
    }

    public Role merge(Role role) {
        return em.merge(role);
    }

    public void delete(Role role) {
        em.remove(em.contains(role) ? role : em.merge(role));
    }
}
