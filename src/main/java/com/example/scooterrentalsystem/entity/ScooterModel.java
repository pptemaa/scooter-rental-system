package com.example.scooterrentalsystem.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "scooter_models")
public class ScooterModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "max_weight")
    private Integer maxWeight;

    public ScooterModel() {
    }

    public ScooterModel(String name, Integer maxWeight) {
        this.name = name;
        this.maxWeight = maxWeight;
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

    public Integer getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Integer max_weight) {
        this.maxWeight = max_weight;
    }
}
