package com.subash.fund.management.service;

import com.subash.fund.management.model.*;
import com.subash.fund.management.repository.*;
import com.subash.fund.management.util.Constants;
import com.subash.fund.management.util.GenericLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static com.subash.fund.management.util.Constants.*;

/**
 * Implementation of {@link OrderService} that handles buying and redeeming mutual fund units.
 * <p>
 * Validates the request, processes the fund NAV, updates user holdings and fund scripts,
 * and logs transactions in the database.
 * </p>
 * Supports two order types:
 * <ul>
 *     <li>BUY – Buys fund units for a user.</li>
 *     <li>REDEEM – Redeems fund units and credits the user.</li>
 * </ul>
 * <p>
 * All actions are logged and validated against the current NAV for the fund.
 *
 * @author Subash
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

    private final FundRepository fundRepository;
    private final FundNavRepository fundNavRepository;
    private final UserHoldingRepository userHoldingRepository;
    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;
    private final GenericLogger genericLogger;

    /**
     * Constructor-based dependency injection for order processing.
     */
    public OrderServiceImpl(FundRepository fundRepository,
                            FundNavRepository fundNavRepository, UserHoldingRepository userHoldingRepository,
                            UserRepository userRepository, TransactionRepository transactionRepository, GenericLogger genericLogger) {
        this.fundRepository = fundRepository;
        this.fundNavRepository = fundNavRepository;
        this.userRepository = userRepository;
        this.userHoldingRepository = userHoldingRepository;
        this.transactionRepository = transactionRepository;
        this.genericLogger = genericLogger;
    }

    /**
     * Processes a mutual fund order (BUY or REDEEM).
     *
     * @param uuid      Unique identifier for tracking the request.
     * @param orderType Type of order to process – either "BUY" or "REDEEM".
     * @param orderView Contains order input details like fund ID, username, units, and NAV.
     * @return {@link ResponseEntity} containing order status and message.
     * @throws Exception if any step of order processing fails.
     */
    @Override
    public ResponseEntity<OrderResponse> createOrder(String uuid, String orderType, OrderView orderView) throws Exception {
        logger.info(uuid + COMMA + LOG_MESSAGE + "Processing create order request");
        OrderResponse orderResponse = new OrderResponse();
        try {
            Optional<User> userOptional = userRepository.findByUsername(orderView.getUsername());
            Optional<FundScript> fundOptional = fundRepository.findById(orderView.getFundId());
            // Validate Username
            if (userOptional.isEmpty()) {
                orderResponse.setCode(USER_RECORD_NOT_FOUND_CODE);
                orderResponse.setMessage(USER_RECORD_NOT_FOUND);
                return new ResponseEntity<>(orderResponse, HttpStatus.BAD_REQUEST);
            }
            // Validate FundId
            if (fundOptional.isEmpty()) {
                orderResponse.setCode(RECORD_NOT_FOUND_CODE);
                orderResponse.setMessage(RECORD_NOT_FOUND);
                return new ResponseEntity<>(orderResponse, HttpStatus.BAD_REQUEST);
            }
            // Validate Nav amount
            Optional<FundNav> fundNavOptional = fundNavRepository.findByFundIdAndNavDate(fundOptional.get(), LocalDate.now());
            if (fundNavOptional.isPresent()) {
                FundNav fundNav = fundNavOptional.get();
                if (!fundNav.getNav().equals(orderView.getNav())) {
                    orderResponse.setCode(FUND_NAV_VALUE_CODE);
                    orderResponse.setMessage(FUND_NAV_VALUE);
                    return new ResponseEntity<>(orderResponse, HttpStatus.BAD_REQUEST);
                }
            }
            Optional<UserHolding> userHoldingOptional = userHoldingRepository.findByUserAndFund(userOptional.get(), fundOptional.get());

            FundScript fundScript = fundOptional.get();
            FundNav fundNav = fundNavOptional.get();
            logger.info(uuid + COMMA + LOG_MESSAGE + "Initiated " + orderType + " Order");
            if (orderType.equalsIgnoreCase(OrderType.REDEEM.name())) {
                // Process REDEEM Order
                return redeemOrder(uuid, userHoldingOptional, fundScript, fundNav, orderView);
            }
            // Process buyOrder
            return buyOrder(uuid, userHoldingOptional, fundScript, fundNav, userOptional.get(), orderView);
        } catch (Exception e) {
            // Logger error response
            genericLogger.logResponse(logger, uuid, "ERROR", Constants.API_PROCESSED_FAILURE);
            throw new Exception(e);
        }
    }

    /**
     * Processes a redeem order by deducting units from user's holdings and updating the fund.
     */
    private ResponseEntity<OrderResponse> redeemOrder(String uuid, Optional<UserHolding> userHoldingOptional, FundScript fundScript, FundNav fundNav, OrderView orderView) {
        OrderResponse orderResponse = new OrderResponse();
        if (userHoldingOptional.isEmpty() || userHoldingOptional.get().getUnits().compareTo(orderView.getUnits()) <= 0) {
            // Bad request - If insufficient units on redeem request
            orderResponse.setCode(INSUFFICIENT_UNITS_USER_CODE);
            orderResponse.setMessage(INSUFFICIENT_UNITS_USER);
            return new ResponseEntity<>(orderResponse, HttpStatus.BAD_REQUEST);
        }
        logger.info(uuid + COMMA + LOG_MESSAGE + "Record Available in UserHolding");
        UserHolding userHolding = userHoldingOptional.get();
        // Reduce units count in userHolding table
        userHolding.setUnits(userHolding.getUnits().subtract(orderView.getUnits()));
        // Reduce totalValue in userHolding table
        BigDecimal unitValue = fundNav.getNav().multiply(orderView.getUnits());
        userHolding.setTotalValue(userHolding.getTotalValue().subtract(unitValue));


        // Add totalUnit count from fundScript table
        fundScript.setTotalUnits(fundScript.getTotalUnits().add(orderView.getUnits()));
        userHoldingRepository.save(userHolding);
        logger.info(uuid + COMMA + LOG_MESSAGE + "Record Saved UserHolding");
        fundRepository.save(fundScript);
        logger.info(uuid + COMMA + LOG_MESSAGE + "Record Saved FundScripts");
        orderResponse.setCode(ORDER_COMPLETED_CODE);
        orderResponse.setMessage(ORDER_COMPLETED);

        // Create an entry in Transaction table
        saveTransactionHistory(fundScript, userHolding.getUser(), orderView, unitValue, "REDEEM");
        logger.info(uuid + COMMA + LOG_MESSAGE + "Record Saved Transaction");
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    /**
     * Processes a buy order by adding units to user's holdings and updating the fund.
     */
    private ResponseEntity<OrderResponse> buyOrder(String uuid, Optional<UserHolding> userHoldingOptional, FundScript fundScript, FundNav fundNav, User user, OrderView orderView) {
        OrderResponse orderResponse = new OrderResponse();
        UserHolding userHolding;
        BigDecimal totalValue;
        if (userHoldingOptional.isPresent()) {
            logger.info(uuid + COMMA + LOG_MESSAGE + "Record Available in UserHolding");
            userHolding = userHoldingOptional.get();
            // Bad request - If insufficient units on buy request
            if (fundScript.getTotalUnits().compareTo(orderView.getUnits()) <= 0) {
                orderResponse.setCode(INSUFFICIENT_UNITS_FUNDS_CODE);
                orderResponse.setMessage(INSUFFICIENT_UNITS_FUNDS);
                return new ResponseEntity<>(orderResponse, HttpStatus.BAD_REQUEST);
            }
            // Add units count in userHolding table
            userHolding.setUnits(userHolding.getUnits().add(orderView.getUnits()));
            // Add totalValue in userHolding table
            totalValue = fundNav.getNav().multiply(orderView.getUnits());
            userHolding.setTotalValue(userHolding.getTotalValue().add(totalValue));
            // Reduce totalUnit count from fundScript table
            fundScript.setTotalUnits(fundScript.getTotalUnits().subtract(orderView.getUnits()));

        } else {
            logger.info(uuid + COMMA + LOG_MESSAGE + "Creating New Record in UserHolding");
            // 1st time buy order creates record in UserHolding.
            userHolding = new UserHolding();
            userHolding.setUser(user);
            userHolding.setFund(fundScript);
            userHolding.setUnits(orderView.getUnits());
            totalValue = fundNav.getNav().multiply(orderView.getUnits());
            userHolding.setTotalValue(totalValue);
            // Reduce totalUnit count from fundScript table
            fundScript.setTotalUnits(fundScript.getTotalUnits().subtract(orderView.getUnits()));

        }
        userHoldingRepository.save(userHolding);
        logger.info(uuid + COMMA + LOG_MESSAGE + "Record Saved UserHolding");
        fundRepository.save(fundScript);
        logger.info(uuid + COMMA + LOG_MESSAGE + "Record Saved FundScripts");
        orderResponse.setTotalValue(totalValue);
        orderResponse.setCode(ORDER_COMPLETED_CODE);
        orderResponse.setMessage(ORDER_COMPLETED);

        // Create an entry in Transaction table
        saveTransactionHistory(fundScript, user, orderView, totalValue, "BUY");
        logger.info(uuid + COMMA + LOG_MESSAGE + "Record Saved Transaction");
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    /**
     * Records the transaction into the transaction table.
     */
    private void saveTransactionHistory(FundScript fundScript, User user, OrderView orderView, BigDecimal amount, String orderType) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setFund(fundScript);
        transaction.setType(orderType);
        transaction.setNav(orderView.getNav());
        transaction.setUnits(orderView.getUnits());
        transaction.setAmount(amount);
        transactionRepository.save(transaction);
    }

}
