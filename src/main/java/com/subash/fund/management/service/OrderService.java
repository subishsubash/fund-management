package com.subash.fund.management.service;

import com.subash.fund.management.model.OrderResponse;
import com.subash.fund.management.model.OrderView;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<OrderResponse> createOrder(String uuid, String orderType, OrderView orderView) throws Exception;
}
