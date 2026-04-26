package com.example.scooterrentalsystem.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email",nullable = false,unique = true)
    private String email;
    @Column(name = "password",nullable = false)
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id",nullable = false)
    private Role role;

    @Column(name = "balance", precision = 12, scale = 2, nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Version
    private Integer version;

    public User() {
    }

    public User(String email, String password, String firstName, String lastName, Role role, BigDecimal balance) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.balance = (balance != null) ? balance : BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getVersion() {
        return version;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
