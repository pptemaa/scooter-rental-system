package com.example.scooterrentalsystem.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "tariffs")
public class Tariff implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TariffType type;
    @Column(name = "price",precision = 10,scale = 2,nullable = false)
    private BigDecimal price;
    @Column(name = "discount", precision = 10, scale = 2)
    private BigDecimal discount = new BigDecimal("0.00");

    public Tariff() {}

    public Tariff(String name, TariffType type, BigDecimal price, BigDecimal discount) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.discount = (discount != null) ? discount : new BigDecimal("0.00");
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TariffType getType() {
        return type;
    }

    public void setType(TariffType type) {
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

    public void setId(Long id) {
        this.id = id;
    }
}
