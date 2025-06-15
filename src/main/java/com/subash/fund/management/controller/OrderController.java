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

@RestController
@RequestMapping("/v1/api/funds")
public class OrderController {

    private static final Logger logger = LogManager.getLogger(OrderController.class);

    private final OrderService orderService;
    private final GenericLogger genericLogger;


    public OrderController(OrderService orderService, GenericLogger genericLogger) {
        this.orderService = orderService;
        this.genericLogger = genericLogger;
    }

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
