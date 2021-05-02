package com.jb.couponSystem.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * An entity class for a user.
 */
@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "user", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Company.class, name = "COMPANY"),
        @JsonSubTypes.Type(value = Customer.class, name = "CUSTOMER"),
        @JsonSubTypes.Type(value = Admin.class, name = "ADMIN")
})

public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
    @Column(nullable = false)
    protected String name;
    @Column(nullable = false)
    protected String email;
    @Column(nullable = false)
    protected String password;
    @Column(nullable = false)
    protected ClientType type;
    private String editedPassword;
    private String confirmPassword;

}
