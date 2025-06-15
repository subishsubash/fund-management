package com.subash.fund.management.repository;

import com.subash.fund.management.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for managing {@link Transaction} entities.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD operations for transaction records.
 * Transactions represent user operations such as buying or redeeming mutual fund units.
 * </p>
 *
 * <p>
 * Example use cases:
 * <ul>
 *   <li>Retrieve all transactions by a user (via custom queries)</li>
 *   <li>Persist new buy/redeem transactions</li>
 *   <li>Audit user investment activities</li>
 * </ul>
 * </p>
 *
 * @see Transaction
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
