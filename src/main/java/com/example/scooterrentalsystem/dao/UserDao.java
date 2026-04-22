package com.example.scooterrentalsystem.dao;

import com.example.scooterrentalsystem.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {
    @PersistenceContext
    private EntityManager em;

    public void save(User user) {
        em.persist(user);
        em.flush();
    }

    public Optional<User> findByEmail(String email) {
        return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    public Optional<User> findByIdWithRole(Long id) {
        List<User> list = em.createQuery(
                        "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.role WHERE u.id = :id",
                        User.class)
                .setParameter("id", id)
                .setMaxResults(1)
                .getResultList();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public List<User> findAllWithRoles() {
        return em.createQuery(
                        "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.role ORDER BY u.id",
                        User.class)
                .getResultList();
    }

    public void delete(User user) {
        em.remove(em.contains(user) ? user : em.merge(user));
    }

    public User merge(User user) {
        return em.merge(user);
    }

    public long countByRoleId(Long roleId) {
        Long c = em.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.role.id = :rid",
                        Long.class)
                .setParameter("rid", roleId)
                .getSingleResult();
        return c != null ? c : 0L;
    }

    public long countRentalsForUser(Long userId) {
        Long c = em.createQuery(
                        "SELECT COUNT(r) FROM Rental r WHERE r.user.id = :uid",
                        Long.class)
                .setParameter("uid", userId)
                .getSingleResult();
        return c != null ? c : 0L;
    }
}
