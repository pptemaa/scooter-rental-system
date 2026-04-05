package com.example.scooterrentalsystem.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rental_points")
public class RentalPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "latitude",precision = 10,scale = 8,nullable = false)
    private BigDecimal latitude;
    @Column(name = "longitude",precision = 11,scale = 8,nullable = false)
    private BigDecimal longitude;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private RentalPoint parent;
    @OneToMany(mappedBy = "parent")
    private List<RentalPoint> subPoints = new ArrayList<>();

    public RentalPoint() {
    }

    public RentalPoint(String name, BigDecimal latitude, BigDecimal longitude, RentalPoint parent) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public RentalPoint getParent() {
        return parent;
    }

    public void setParent(RentalPoint parent) {
        this.parent = parent;
    }

    public List<RentalPoint> getSubPoints() {
        return subPoints;
    }

    public void setSubPoints(List<RentalPoint> subPoints) {
        this.subPoints = subPoints;
    }
}
