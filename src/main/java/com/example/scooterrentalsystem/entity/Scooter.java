package com.example.scooterrentalsystem.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "scooters")
public class Scooter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id",nullable = false)
    private ScooterModel scooterModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_point_id")
    private RentalPoint rentalPoint;
    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private Status status;
    @Column(name = "mileage", precision = 8, scale = 2)
    private BigDecimal mileage = new BigDecimal("0.00");

    @Version
    private Integer version;

    public Scooter() {}

    public Scooter(ScooterModel model, RentalPoint rentalPoint, Status status, BigDecimal mileage) {
        this.scooterModel = model;
        this.rentalPoint = rentalPoint;
        this.status = status;
        this.mileage = (mileage != null) ? mileage : new BigDecimal("0.00");
    }

    public ScooterModel getScooterModel() {
        return scooterModel;
    }

    public void setScooterModel(ScooterModel scooterModel) {
        this.scooterModel = scooterModel;
    }

    public RentalPoint getRentalPoint() {
        return rentalPoint;
    }

    public void setRentalPoint(RentalPoint rentalPoint) {
        this.rentalPoint = rentalPoint;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getMileage() {
        return mileage;
    }

    public void setMileage(BigDecimal mileage) {
        this.mileage = mileage;
    }

    public Long getId() {
        return id;
    }

}
