package com.subash.fund.management.repository;

import com.subash.fund.management.model.FundScript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for managing {@link FundScript} entities.
 * <p>
 * Extends {@link JpaRepository} to provide basic CRUD operations for mutual fund metadata.
 * This repository operates on the {@code funds_scripts} table with {@code fundId} as the primary key.
 * </p>
 *
 * <p>
 * Example use case: Fetch fund details by fund ID, save new fund scripts, update fund metadata.
 * </p>
 *
 * @see FundScript
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface FundRepository extends JpaRepository<FundScript, String> {
}
