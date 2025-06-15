package com.subash.fund.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subash.fund.management.config.TestSecurityConfig;
import com.subash.fund.management.model.FundNavView;
import com.subash.fund.management.model.FundResponse;
import com.subash.fund.management.model.FundView;
import com.subash.fund.management.service.FundService;
import com.subash.fund.management.util.GenericLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test class for {@link com.subash.fund.management.controller.FundController}.
 * <p>
 * This class is focused on testing the REST endpoints related to fund creation and NAV updates.
 * It uses {@link WebMvcTest} to test only the web layer and excludes service and repository layers.
 * <p>
 * Security configurations are mocked using {@link TestSecurityConfig}, and services are injected as mocks
 * using {@link MockitoBean}. HTTP request behavior is simulated using {@link MockMvc}.
 * </p>
 *
 * <p>Test scenarios covered include:</p>
 * <ul>
 *   <li>Successful fund creation</li>
 *   <li>Successful fund NAV update</li>
 *   <li>Bad requests due to invalid or missing input</li>
 * </ul>
 */
@WebMvcTest(FundController.class)
@Import(TestSecurityConfig.class)
class FundControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FundService fundService;

    @MockitoBean
    private GenericLogger genericLogger;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test for successful fund creation.
     * <p>
     * Verifies that a POST request to `/v1/api/funds` with valid input returns a 200 OK response
     * and the expected success message.
     * </p>
     */
    @Test
    @DisplayName("POST /v1/api/funds - Create Fund - Success")
    void testCreateFund_Success() throws Exception {
        FundView fundView = new FundView();
        fundView.setFundName("Nippon Index Fund");
        fundView.setFundId("35435343633");
        fundView.setNavDate(LocalDate.now());
        fundView.setTotalUnits(BigDecimal.valueOf(4820));
        fundView.setNav(BigDecimal.valueOf(123.45));

        FundResponse response = new FundResponse();
        response.setCode(5001);
        response.setMessage("Created Successful");

        Mockito.when(fundService.createFund(anyString(), any(FundView.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(post("/v1/api/funds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fundView)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(5001))
                .andExpect(jsonPath("$.message").value("Created Successful"));
    }


    /**
     * Test for successful NAV update of a fund.
     * <p>
     * Sends a PUT request to `/v1/api/funds/{fundId}` and verifies response status and content.
     * </p>
     */
    @Test
    @DisplayName("PUT /v1/api/funds/{fundId} - Update Fund NAV - Success")
    void testUpdateFund_Success() throws Exception {
        String fundId = "35435343633";
        FundNavView navView = new FundNavView();
        navView.setNavDate(LocalDate.now());
        navView.setNav(BigDecimal.valueOf(200.50));

        FundResponse response = new FundResponse();
        response.setCode(5002);
        response.setMessage("Updated Successful");

        Mockito.when(fundService.updateFund(anyString(), anyString(), any(FundNavView.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(put("/v1/api/funds/{fundId}", fundId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(navView)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(5002))
                .andExpect(jsonPath("$.message").value("Updated Successful"));
    }

    /**
     * Test for bad request when creating a fund with missing or invalid fields.
     * <p>
     * Verifies that POST request to `/v1/api/funds` with empty payload results in 400 Bad Request.
     * </p>
     */
    @Test
    @DisplayName("POST /v1/api/funds - Create Fund - Bad Request (Missing Fields)")
    void testCreateFund_BadRequest() throws Exception {
        FundView invalidFund = new FundView(); // Missing required fields

        mockMvc.perform(post("/v1/api/funds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidFund)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test for bad request when updating NAV with incomplete or invalid data.
     * <p>
     * Expects 400 Bad Request due to validation failure on missing NAV field.
     * </p>
     */
    @Test
    @DisplayName("PUT /v1/api/funds/{fundId} - Update Fund NAV - Bad Request (Invalid NAV)")
    void testUpdateFund_BadRequest() throws Exception {
        FundNavView invalidNav = new FundNavView(); // Missing nav value

        mockMvc.perform(put("/v1/api/funds/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidNav)))
                .andExpect(status().isBadRequest());
    }
}