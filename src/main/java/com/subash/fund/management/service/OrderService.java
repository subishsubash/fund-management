package com.subash.fund.management.service;

import com.subash.fund.management.model.OrderResponse;
import com.subash.fund.management.model.OrderView;
import org.springframework.http.ResponseEntity;

/**
 * Service interface for handling mutual fund order-related operations.
 * <p>
 * Provides contract for placing orders such as buying or redeeming mutual fund units.
 * Implementing classes should handle validation, business logic, and persistence as required.
 *
 * <p>Typical usage scenarios:
 * <ul>
 *     <li>Buy new units of a fund</li>
 *     <li>Redeem (sell) existing units of a fund</li>
 * </ul>
 *
 * @author Subash
 * @see com.subash.fund.management.service.impl.OrderServiceImpl
 * @see com.subash.fund.management.model.OrderView
 * @see com.subash.fund.management.model.OrderResponse
 */
public interface OrderService {

    /**
     * Places a new order (BUY or REDEEM) for the authenticated user.
     *
     * @param uuid       Unique identifier for the request (used for logging and tracing)
     * @param orderType  Type of order to place ("BUY" or "REDEEM")
     * @param orderView  Contains order details such as username, fund ID, and units
     * @return A {@link ResponseEntity} containing the order response and HTTP status code
     * @throws Exception if any error occurs during order creation (validation, database, etc.)
     */
    ResponseEntity<OrderResponse> createOrder(String uuid, String orderType, OrderView orderView) throws Exception;
}
