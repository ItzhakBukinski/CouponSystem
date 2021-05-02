package com.jb.couponSystem.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * An entity class for a company.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Company extends User {
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Coupon> coupons;
    @Column(nullable = false)
    private final ClientType type = ClientType.COMPANY;

    public Company(String name, String email, String password) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
