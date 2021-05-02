package com.jb.couponSystem.data.repository;

import com.jb.couponSystem.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A {@link JpaRepository} interface for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find a user by its email and password.
     *
     * @param email    the email.
     * @param password the password.
     * @return an {@link Optional}. if found contains a user, otherwise empty.
     */
    Optional<? extends User> findByEmailAndPassword(String email, String password);
}
