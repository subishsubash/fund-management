package com.subash.fund.management.service;

import com.subash.fund.management.model.FundNavView;
import com.subash.fund.management.model.FundResponse;
import com.subash.fund.management.model.FundView;
import org.springframework.http.ResponseEntity;

/**
 * Service interface for handling business logic related to mutual fund operations.
 * <p>
 * Provides methods to create and update mutual fund data. Each method accepts a unique identifier
 * (usually for traceability/logging), and corresponding request models to perform the operations.
 * </p>
 *
 * <p>
 * This interface is typically implemented by a class annotated with {@code @Service} to handle:
 * <ul>
 *   <li>Validation and processing of fund-related requests</li>
 *   <li>Interactions with the database via repositories</li>
 *   <li>Returning appropriate {@link ResponseEntity} responses</li>
 * </ul>
 * </p>
 *
 * @see FundView
 * @see FundNavView
 * @see FundResponse
 */
public interface FundService {

    /**
     * Creates a new mutual fund based on the given fund view data.
     *
     * @param uuid     A unique identifier for logging or traceability
     * @param fundView The fund details to be registered
     * @return A {@link ResponseEntity} containing the creation result and status
     * @throws Exception if fund creation fails due to validation or persistence errors
     */
    ResponseEntity<FundResponse> createFund(String uuid, FundView fundView) throws Exception;

    /**
     * Updates an existing fund's NAV using the given fund ID and NAV view.
     *
     * @param uuid         A unique identifier for logging or traceability
     * @param fundId       The unique ID of the fund to update
     * @param fundNavView  The updated NAV details
     * @return A {@link ResponseEntity} containing the update result and status
     * @throws Exception if update fails due to invalid input or missing fund
     */
    ResponseEntity<FundResponse> updateFund(String uuid, String fundId, FundNavView fundNavView) throws Exception;

}
