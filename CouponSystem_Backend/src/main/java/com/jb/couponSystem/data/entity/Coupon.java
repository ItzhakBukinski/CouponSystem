package com.jb.couponSystem.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * An entity class for a coupon.
 */
@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private Category category;
    private int amount;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private double price;
    private String imageURL;
    private String logo;
    @Column(nullable = false)
    private int sales;
    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Company company;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany
    @JoinTable(name = "customer_coupon",
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id"),
            uniqueConstraints = @UniqueConstraint(columnNames
                    = {"customer_id", "coupon_id"}))
    private List<Customer> customers;

    public Coupon(String title, LocalDate startDate, LocalDate endDate,
                  Category category, int amount, String description, double price, String imageURL) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.price = price;
        this.imageURL = imageURL;
    }


    /**
     * Checks if coupon is in stock.
     *
     * @return true if amount is non positive - meaning the coupon is out of stock, otherwise false.
     */
    public boolean isInStock() {
        return amount > 0;
    }

}

