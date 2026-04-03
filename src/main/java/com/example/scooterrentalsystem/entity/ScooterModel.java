package com.example.scooterrentalsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "scooter_models")
public class ScooterModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "max_weight")
    private Integer max_weight;

    public ScooterModel() {
    }

    public ScooterModel(String name, Integer max_weight) {
        this.name = name;
        this.max_weight = max_weight;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMax_weight() {
        return max_weight;
    }

    public void setMax_weight(Integer max_weight) {
        this.max_weight = max_weight;
    }
}
