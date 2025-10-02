package com.indomarco.indostore.repository;

import com.indomarco.indostore.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for User entity.
 * 
 * Provides standard CRUD operations and query methods for User.
 * 
 * Additionally, this repository defines custom query methods:
 * {@link #findByEmail(String)} - returns an Optional containing the user with the given email.
 * {@link #findByToken(String)} - returns an Optional containing the user with the given token.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByToken(String token);
}
