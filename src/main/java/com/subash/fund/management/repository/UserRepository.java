package com.subash.fund.management.repository;

import com.subash.fund.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link User} entities.
 * <p>
 * Extends {@link JpaRepository} to provide default implementations for common persistence methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a user by their unique username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the {@link User} if found, or empty if not found
     */
    Optional<User> findByUsername(String username);
}
