package com.jb.couponSystem.data.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * An entity class for an admin.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Admin extends User {
    @Column(nullable = false)
    private final ClientType type = ClientType.ADMIN;
}
