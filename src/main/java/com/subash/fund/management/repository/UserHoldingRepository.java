package com.subash.fund.management.repository;

import com.subash.fund.management.model.FundScript;
import com.subash.fund.management.model.User;
import com.subash.fund.management.model.UserHolding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link UserHolding} entities.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD operations for tracking how many units
 * of a mutual fund a user owns, along with the total value of their holdings.
 * </p>
 *
 * <p>
 * Example use cases:
 * <ul>
 *   <li>Check if a user already has holdings in a specific fund</li>
 *   <li>Update the user's holdings after a transaction</li>
 *   <li>Retrieve holding details for portfolio summary</li>
 * </ul>
 * </p>
 *
 * @see UserHolding
 * @see User
 * @see FundScript
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface UserHoldingRepository extends JpaRepository<UserHolding, Long> {
    Optional<UserHolding> findByUserAndFund(User user, FundScript fund);

}
