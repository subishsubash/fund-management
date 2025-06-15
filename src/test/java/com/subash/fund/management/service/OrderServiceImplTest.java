package com.subash.fund.management.service;

import com.subash.fund.management.model.*;
import com.subash.fund.management.repository.*;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test class for {@link com.subash.fund.management.service.OrderServiceImpl}.
 * <p>
 * This test class verifies the functionality of the mutual fund order placement service
 * (buy and redeem operations), covering various success and failure scenarios.
 * </p>
 *
 * <p>Dependencies are mocked using Mockito for isolation of business logic testing.
 * Assertions are used to validate proper HTTP status codes, business response codes,
 * and behavior under different edge and error cases.</p>
 *
 * <p>Key Scenarios Covered:</p>
 * <ul>
 *     <li>Successful fund buy order</li>
 *     <li>Invalid user or fund ID</li>
 *     <li>NAV mismatch during transaction</li>
 *     <li>Insufficient fund units for buy or redeem</li>
 *     <li>Exception handling during order processing</li>
 * </ul>
 *
 * @see com.subash.fund.management.service.OrderServiceImpl
 * @see com.subash.fund.management.model.OrderView
 * @see com.subash.fund.management.model.OrderResponse
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private FundRepository fundRepository;
    @Mock
    private FundNavRepository fundNavRepository;
    @Mock
    private UserHoldingRepository userHoldingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private GenericLogger genericLogger;

    @InjectMocks
    private OrderServiceImpl orderService;

    private final String uuid = "uuid123";
    private final static String fundId = "2342323545";
    private final static String username = "subish12396";
    private final LocalDate today = LocalDate.now();

    private static OrderView orderView;
    private static User user;
    private static FundScript fundScript;
    private static FundNav fundNav;
    private static UserHolding userHolding;

    /**
     * Initializes shared mock objects before running tests.
     */
    @BeforeAll
    static void setUp() {
        orderView = new OrderView();
        orderView.setNav(BigDecimal.valueOf(232.1));
        orderView.setUsername(username);
        orderView.setFundId(fundId);
        orderView.setUnits(BigDecimal.valueOf(10));
        user = new User();
        user.setUsername(username);

        fundScript = new FundScript();
        fundScript.setFundName("Nippon Index Fund");
        fundScript.setFundId(fundId);
        fundScript.setTotalUnits(BigDecimal.valueOf(4820));

        fundNav = new FundNav();
        fundNav.setFundId(fundScript);
        fundNav.setNavDate(LocalDate.now());
        fundNav.setNav(BigDecimal.valueOf(232.1));

        userHolding = new UserHolding();
        userHolding.setTotalValue(BigDecimal.valueOf(729.33));
        userHolding.setFund(fundScript);
        userHolding.setUser(user);
        userHolding.setUnits(BigDecimal.valueOf(67));

    }

    /**
     * Test case: Successfully processes a BUY order.
     */
    @Test
    void createOrder_shouldProcessBuyOrderSuccessfully() throws Exception {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(fundRepository.findById(fundId)).thenReturn(Optional.of(fundScript));
        when(fundNavRepository.findByFundIdAndNavDate(any(), eq(today))).thenReturn(Optional.of(fundNav));
        when(userHoldingRepository.findByUserAndFund(user, fundScript)).thenReturn(Optional.empty());

        ResponseEntity<OrderResponse> response = orderService.createOrder(uuid, "BUY", orderView);

        assertEquals(HttpStatus.CREATED, ((ResponseEntity<?>) response).getStatusCode());
        assertEquals(5010, response.getBody().getCode()); // ORDER_COMPLETED_CODE
    }

    /**
     * Test case: Fails when user is not found.
     */
    @Test
    void createOrder_shouldReturnBadRequest_ifUserNotFound() throws Exception {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        ResponseEntity<OrderResponse> response = orderService.createOrder(uuid, "BUY", orderView);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(5004, response.getBody().getCode()); // USER_RECORD_NOT_FOUND_CODE
    }

    /**
     * Test case: Fails when fund ID is not valid.
     */
    @Test
    void createOrder_shouldReturnBadRequest_ifFundNotFound() throws Exception {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));
        when(fundRepository.findById(fundId)).thenReturn(Optional.empty());
        ResponseEntity<OrderResponse> response = orderService.createOrder(uuid, "BUY", orderView);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(5006, response.getBody().getCode()); // RECORD_NOT_FOUND_CODE
    }

    /**
     * Test case: NAV in request does not match with actual NAV from the database.
     */
    @Test
    void createOrder_shouldReturnBadRequest_ifNavMismatch() throws Exception {
        User user = new User();
        FundScript fundScript = new FundScript();
        FundNav fundNav = new FundNav();
        fundNav.setNav(BigDecimal.valueOf(100));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(fundRepository.findById(fundId)).thenReturn(Optional.of(fundScript));
        when(fundNavRepository.findByFundIdAndNavDate(any(), eq(today))).thenReturn(Optional.of(fundNav));

        ResponseEntity<OrderResponse> response = orderService.createOrder(uuid, "BUY", orderView);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(5005, response.getBody().getCode()); // FUND_NAV_VALUE_CODE
    }

    /**
     * Test case: Buy fails due to insufficient fund units.
     */
    @Test
    void createOrder_shouldReturnBadRequest_ifInsufficientFundUnitsOnBuy() throws Exception {
        fundScript.setTotalUnits(BigDecimal.valueOf(1));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(fundRepository.findById(fundId)).thenReturn(Optional.of(fundScript));
        when(fundNavRepository.findByFundIdAndNavDate(fundScript, today)).thenReturn(Optional.of(fundNav));
        when(userHoldingRepository.findByUserAndFund(user, fundScript)).thenReturn(Optional.of(userHolding));

        ResponseEntity<OrderResponse> response = orderService.createOrder(uuid, "BUY", orderView);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(5009, response.getBody().getCode()); // INSUFFICIENT_UNITS_FUNDS_CODE
    }

    /**
     * Test case: Redeem fails due to insufficient user units.
     */
    @Test
    void createOrder_shouldReturnBadRequest_ifInsufficientUnitsOnRedeem() throws Exception {
        userHolding.setUnits(BigDecimal.valueOf(1));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(fundRepository.findById(fundId)).thenReturn(Optional.of(fundScript));
        when(fundNavRepository.findByFundIdAndNavDate(fundScript, today)).thenReturn(Optional.of(fundNav));
        when(userHoldingRepository.findByUserAndFund(user, fundScript)).thenReturn(Optional.of(userHolding));

        ResponseEntity<OrderResponse> response = orderService.createOrder(uuid, "REDEEM", orderView);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(5008, response.getBody().getCode());
    }

    /**
     * Test case: Handles unexpected exception during order processing and logs error.
     */
    @Test
    void createOrder_shouldHandleExceptionDuringProcessing() {
        when(userRepository.findByUsername(anyString())).thenThrow(new RuntimeException("DB Failure"));

        assertThrows(Exception.class, () -> orderService.createOrder(uuid, "BUY", orderView));
        verify(genericLogger).logResponse(any(), eq(uuid), eq("ERROR"), any());
    }
}