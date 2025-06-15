package com.subash.fund.management.repository;

import com.subash.fund.management.model.FundNav;
import com.subash.fund.management.model.FundScript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;


/**
 * Repository interface for managing {@link FundNav} entities.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD operations and custom query methods
 * for accessing NAV (Net Asset Value) data associated with mutual funds.
 * </p>
 *
 * <p>
 * Example use case: Retrieve the NAV of a specific fund on a given date.
 * </p>
 *
 * @see FundNav
 * @see FundScript
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface FundNavRepository extends JpaRepository<FundNav, Long> {
    /**
     * Fetches the NAV entry for a given fund and NAV date.
     *
     * @param fundId The fund for which the NAV is being queried.
     * @param date   The NAV date to look up.
     * @return An {@link Optional} containing the {@link FundNav} entry if found, or empty if not.
     */
    Optional<FundNav> findByFundIdAndNavDate(FundScript fundId, LocalDate date);
}
