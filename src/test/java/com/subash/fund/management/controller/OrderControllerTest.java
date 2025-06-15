package com.subash.fund.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subash.fund.management.config.TestSecurityConfig;
import com.subash.fund.management.model.OrderResponse;
import com.subash.fund.management.model.OrderView;
import com.subash.fund.management.service.OrderService;
import com.subash.fund.management.util.GenericLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Unit test class for {@link com.subash.fund.management.controller.OrderController}.
 * <p>
 * This test class uses {@link WebMvcTest} to focus only on the web layer,
 * testing the controller's behavior without starting the full application context.
 * It verifies multiple scenarios for creating orders, including:
 * successful order creation, authorization failure, bad requests, and internal server errors.
 * </p>
 *
 * <p>
 * Dependencies such as {@link OrderService} and {@link GenericLogger} are mocked using {@link MockitoBean}.
 * Security context is configured via {@link TestSecurityConfig}, and test users are mocked using {@link WithMockUser}.
 * </p>
 *
 */
@WebMvcTest(OrderController.class)
@Import(TestSecurityConfig.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private GenericLogger genericLogger;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderView validOrderView;

    /**
     * Initializes the test input before each test case execution.
     * Prepares a valid {@link OrderView} object for testing.
     */
    @BeforeEach
    void setUp() {
        validOrderView = new OrderView();
        validOrderView.setUsername("subish12396");
        validOrderView.setFundId("749739330349");
        validOrderView.setUnits(BigDecimal.valueOf(1000.00));
        validOrderView.setNav(BigDecimal.valueOf(127.89));
    }

    /**
     * Tests successful order creation scenario.
     * Expects a 200 OK status and response body containing a 201 code with success message.
     */
    @WithMockUser(username = "subish12396", roles = "USER")
    @Test
    @DisplayName("POST /v1/api/funds/order - Create Order - Success")
    void testCreateOrder_Success() throws Exception {
        OrderResponse response = new OrderResponse();
        response.setCode(201);
        response.setMessage("Order Placed Successfully");

        Mockito.when(orderService.createOrder(anyString(), anyString(), any(OrderView.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(post("/v1/api/funds/order")
                        .param("orderType", "BUY")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validOrderView)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value("Order Placed Successfully"));
    }

    /**
     * Tests forbidden access scenario when the request's username doesn't match the authenticated user.
     * Expects 403 Forbidden with relevant error message.
     */
    @WithMockUser(username = "misMatchUser", roles = "USER")
    @Test
    @DisplayName("POST /v1/api/funds/order - Forbidden When Username Mismatch")
    void testCreateOrder_Forbidden_UsernameMismatch() throws Exception {
        validOrderView.setUsername("otherUser");

        mockMvc.perform(post("/v1/api/funds/order")
                        .param("orderType", "BUY")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validOrderView)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("403"))
                .andExpect(jsonPath("$.message").value("Access denied: You are not authorized to create an order for another user."));
    }

    /**
     * Tests bad request scenario where required fields are missing or invalid.
     * This simulates validation failure.
     */
    @WithMockUser(username = "subish12396", roles = "USER")
    @Test
    @DisplayName("POST /v1/api/funds/order - Bad Request on Missing Fields")
    void testCreateOrder_BadRequest() throws Exception {

        validOrderView.setFundId("343");
        mockMvc.perform(post("/v1/api/funds/order")
                        .param("orderType", "SELL")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validOrderView)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests internal server error scenario where an unhandled exception occurs in the service layer.
     * Expects a 500 Internal Server Error status code.
     */
    @WithMockUser(username = "subish12396", roles = "USER")
    @Test
    @DisplayName("POST /v1/api/funds/order - Error Case: Internal Exception")
    void testCreateOrder_InternalServerError() throws Exception {
        Mockito.when(orderService.createOrder(anyString(), anyString(), any(OrderView.class)))
                .thenThrow(new RuntimeException("Internal error"));

        mockMvc.perform(post("/v1/api/funds/order")
                        .param("orderType", "REDEEM")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validOrderView)))
                .andExpect(status().isInternalServerError());
    }
}