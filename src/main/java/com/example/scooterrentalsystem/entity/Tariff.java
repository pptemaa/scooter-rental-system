package com.example.scooterrentalsystem.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tariffs")
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name",nullable = false,unique = true)
    private String name;
    @Column(name = "type",nullable = false,unique = true)
    private String type;
    @Column(name = "price",precision = 10,scale = 2,nullable = false)
    private BigDecimal price;
    @Column(name = "discount", precision = 10, scale = 2)
    private BigDecimal discount = new BigDecimal("0.00");

    public Tariff() {}

    public Tariff(String name, String type, BigDecimal price, BigDecimal discount) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.discount = (discount != null) ? discount : new BigDecimal("0.00");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
