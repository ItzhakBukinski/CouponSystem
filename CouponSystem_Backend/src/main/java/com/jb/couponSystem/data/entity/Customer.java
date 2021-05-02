package com.jb.couponSystem.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * An entity class for a customer.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Customer extends User {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany
    @JoinTable(name = "customer_coupon",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"customer_id", "coupon_id"}))
    private List<Coupon> coupons;
    @Column(nullable = false)
    private final ClientType type = ClientType.CUSTOMER;

    public Customer(String name, String email, String password) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
