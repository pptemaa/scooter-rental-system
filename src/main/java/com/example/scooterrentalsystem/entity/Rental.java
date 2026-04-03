package com.example.scooterrentalsystem.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scooter_id",nullable = false)
    private Scooter scooter;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_id",nullable = false)
    private Tariff tariff;
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_point_id", nullable = false)
    private RentalPoint startPoint;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_point_id")
    private RentalPoint endPoint;
    @Column(name = "total_cost", precision = 10, scale = 2)
    private BigDecimal totalCost;

    public Rental() {
    }

    public Rental(User user, Scooter scooter, Tariff tariff, LocalDateTime startTime, RentalPoint startPoint) {
        this.user = user;
        this.scooter = scooter;
        this.tariff = tariff;
        this.startTime = startTime;
        this.startPoint = startPoint;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Scooter getScooter() {
        return scooter;
    }

    public void setScooter(Scooter scooter) {
        this.scooter = scooter;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public RentalPoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(RentalPoint startPoint) {
        this.startPoint = startPoint;
    }

    public RentalPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(RentalPoint endPoint) {
        this.endPoint = endPoint;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}
