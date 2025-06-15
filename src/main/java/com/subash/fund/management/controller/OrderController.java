package com.subash.fund.management.controller;

import com.subash.fund.management.model.OrderResponse;
import com.subash.fund.management.model.OrderView;
import com.subash.fund.management.service.OrderService;
import com.subash.fund.management.util.Constants;
import com.subash.fund.management.util.GenericLogger;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.subash.fund.management.util.Constants.*;

/**
 * REST controller responsible for handling order-related operations for funds.
 * <p>
 * This controller validates user identity and processes fund buy/sell orders
 * using the {@link OrderService}. All requests and responses are logged using
 * the {@link GenericLogger}.
 * </p>
 *
 */
@RestController
@RequestMapping("/v1/api/funds")
public class OrderController {

    private static final Logger logger = LogManager.getLogger(OrderController.class);

    private final OrderService orderService;
    private final GenericLogger genericLogger;


    /**
     * Constructs a new {@code OrderController} with the given service and logger.
     *
     * @param orderService   service responsible for order processing
     * @param genericLogger  utility for standardized logging
     */
    public OrderController(OrderService orderService, GenericLogger genericLogger) {
        this.orderService = orderService;
        this.genericLogger = genericLogger;
    }

    /**
     * Creates a new order (e.g., BUY or SELL) for a given user and fund.
     * <p>
     * This method validates that the authenticated user matches the username in the request
     * before processing the order. If the validation fails, the response is {@code 403 FORBIDDEN}.
     * </p>
     *
     * @param orderType the type of order to create (e.g., "BUY", "SELL")
     * @param orderView the order request payload containing user and fund details
     * @return {@link ResponseEntity} containing order processing response and status
     * @throws Exception if order creation fails internally
     *
     * @apiNote Endpoint: {@code POST /v1/api/funds/order}
     */
    @PostMapping("/order")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestParam("orderType") String orderType, @Valid @RequestBody OrderView orderView) throws Exception {
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!authenticatedUsername.equals(orderView.getUsername())) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setMessage(ACCESS_DENIED);
            orderResponse.setCode(ACCESS_DENIED_CODE);
            return new ResponseEntity<>(orderResponse, HttpStatus.FORBIDDEN);
        }

        String uuid = GenericLogger.getUUID();
        logger.info(uuid + COMMA + LOG_MESSAGE + "Request received to created order for Type : " + orderType);
        //Log request
        genericLogger.logRequest(logger, uuid, Constants.CREATE_ORDER, Constants.POST_METHOD, orderView);
        ResponseEntity<OrderResponse> orderResponse = orderService.createOrder(uuid, orderType, orderView);
        //Log response
        genericLogger.logResponse(logger, uuid, orderResponse.getStatusCode().toString(), orderResponse);
        logger.info(uuid + COMMA + LOG_MESSAGE + "Order creation request completed for Type : " + orderType);
        return orderResponse;
    }
}
