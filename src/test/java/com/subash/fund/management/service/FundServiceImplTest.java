package com.subash.fund.management.service;

import com.subash.fund.management.model.*;
import com.subash.fund.management.repository.FundNavRepository;
import com.subash.fund.management.repository.FundRepository;
import com.subash.fund.management.util.GenericLogger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test class for {@link com.subash.fund.management.service.FundServiceImpl}.
 * <p>
 * This test class verifies the correctness of fund creation and NAV update operations,
 * mocking dependencies like {@link com.subash.fund.management.repository.FundRepository},
 * {@link com.subash.fund.management.repository.FundNavRepository}, and {@link com.subash.fund.management.util.GenericLogger}.
 * </p>
 *
 * <p>Covered scenarios include:</p>
 * <ul>
 *   <li>Creating a new fund when it does not exist</li>
 *   <li>Attempting to create a fund that already exists</li>
 *   <li>Exception handling during fund creation</li>
 *   <li>Updating NAV for an existing fund</li>
 *   <li>Attempting to update NAV for a non-existent fund</li>
 *   <li>Exception handling during NAV update</li>
 * </ul>
 *
 * <p>Frameworks used:</p>
 * <ul>
 *   <li>JUnit 5</li>
 *   <li>Mockito</li>
 *   <li>Spring HTTP response validation</li>
 * </ul>
 *
 * @see com.subash.fund.management.service.FundServiceImpl
 * @see com.subash.fund.management.model.FundView
 * @see com.subash.fund.management.model.FundNavView
 * @see com.subash.fund.management.model.FundResponse
 */
@ExtendWith(MockitoExtension.class)
class FundServiceImplTest {

    @Mock
    private FundRepository fundRepository;

    @Mock
    private FundNavRepository fundNavRepository;

    @Mock
    private GenericLogger genericLogger;

    @InjectMocks
    private FundServiceImpl fundService;

    private final String uuid = "test-uuid";
    private final static String fundId = "63838393030";
    private static FundView fundView;
    private static FundScript fundScript;
    private static FundNav fundNav;
    private static FundNavView navView;

    /**
     * Initializes reusable objects before any test runs.
     */
    @BeforeAll
    static void setUp() {
        fundView = new FundView();
        fundView.setFundName("Nippon Index Fund");
        fundView.setFundId(fundId);
        fundView.setNavDate(LocalDate.now());
        fundView.setTotalUnits(BigDecimal.valueOf(4820));
        fundView.setNav(BigDecimal.valueOf(123.45));

        fundScript = new FundScript();
        fundScript.setFundName("Nippon Index Fund");
        fundScript.setFundId(fundId);
        fundScript.setTotalUnits(BigDecimal.valueOf(4820));

        fundNav = new FundNav();
        fundNav.setFundId(fundScript);
        fundNav.setNavDate(LocalDate.now());
        fundNav.setNav(BigDecimal.valueOf(123.45));

        navView = new FundNavView();
        navView.setNavDate(LocalDate.now());
        navView.setNav(BigDecimal.valueOf(124.45));

    }

    /**
     * Test case: Creates a new fund when it does not already exist.
     * Verifies that HTTP status is CREATED and success code is returned.
     */
    @Test
    void testCreateFund_shouldCreateNewFund() throws Exception {
        when(fundRepository.findById(fundId)).thenReturn(Optional.empty());
        when(fundRepository.save(any())).thenReturn(fundScript);
        ResponseEntity<FundResponse> response = fundService.createFund(uuid, fundView);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(5001, response.getBody().getCode());
    }

    /**
     * Test case: Attempts to create a fund that already exists.
     * Should return a response indicating the fund already exists.
     */
    @Test
    void testCreateFund_whenFundExists_shouldReturnExistCode() throws Exception {
        when(fundRepository.findById(fundId)).thenReturn(Optional.of(new FundScript()));
        ResponseEntity<FundResponse> response = fundService.createFund(uuid, fundView);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5002, response.getBody().getCode());
    }

    /**
     * Test case: Simulates an exception during fund creation (e.g., database down).
     * Asserts that exception is thrown and logger is invoked.
     */
    @Test
    void testCreateFund_shouldHandleException() {
        when(fundRepository.findById(fundId)).thenThrow(new RuntimeException("DB down"));
        assertThrows(Exception.class, () -> fundService.createFund(uuid, fundView));
        verify(genericLogger).logResponse(any(), eq(uuid), eq("ERROR"), any());
    }

    /**
     * Test case: Successfully updates NAV for an existing fund.
     * Validates that NAV update returns the correct success code.
     */
    @Test
    void testUpdateFund_shouldUpdateNavSuccessfully() throws Exception {
        when(fundRepository.findById(fundId)).thenReturn(Optional.of(fundScript));
        ResponseEntity<FundResponse> response = fundService.updateFund(uuid, fundId, navView);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(5003, response.getBody().getCode());
    }

    /**
     * Test case: Attempts to update NAV for a fund that does not exist.
     * Validates that an appropriate error code and message are returned.
     */
    @Test
    void testUpdateFund_whenFundNotFound_shouldReturnNotFound() throws Exception {
        when(fundRepository.findById(fundId)).thenReturn(Optional.empty());
        ResponseEntity<FundResponse> response = fundService.updateFund(uuid, fundId, navView);
        assertEquals(5006, response.getBody().getCode());
        assertEquals("Requested fund details are unavailable.", response.getBody().getMessage());
    }

    /**
     * Test case: Simulates an exception during NAV update.
     * Asserts that exception is thrown and logger is triggered.
     */
    @Test
    void testUpdateFund_shouldHandleException() {
        when(fundRepository.findById(fundId)).thenThrow(new RuntimeException("NAV update error"));
        assertThrows(Exception.class, () -> fundService.updateFund(uuid, fundId, navView));
        verify(genericLogger).logResponse(any(), eq(uuid), eq("ERROR"), any());
    }
}
