package com.subash.fund.management.service;

import com.subash.fund.management.model.*;
import com.subash.fund.management.repository.FundNavRepository;
import com.subash.fund.management.repository.FundRepository;
import com.subash.fund.management.repository.UserHoldingRepository;
import com.subash.fund.management.repository.UserRepository;
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

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

    private final FundRepository fundRepository;
    private final FundNavRepository fundNavRepository;
    private final UserHoldingRepository userHoldingRepository;
    private final UserRepository userRepository;
    private final GenericLogger genericLogger;

    public OrderServiceImpl(FundRepository fundRepository,
                            FundNavRepository fundNavRepository, UserHoldingRepository userHoldingRepository,
                            UserRepository userRepository, GenericLogger genericLogger) {
        this.fundRepository = fundRepository;
        this.fundNavRepository = fundNavRepository;
        this.userRepository = userRepository;
        this.userHoldingRepository = userHoldingRepository;
        this.genericLogger = genericLogger;
    }

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
            if (orderType.equalsIgnoreCase(OrderType.SELL.name())) {
                // Process sellOrder
                return sellOrder(userHoldingOptional, fundScript, fundNav, orderView);
            }
            // Process buyOrder
            return buyOrder(userHoldingOptional, fundScript, fundNav, userOptional.get(), orderView);
        } catch (Exception e) {
            // Logger error response
            genericLogger.logResponse(logger, uuid, "ERROR", Constants.API_PROCESSED_FAILURE);
            throw new Exception(e);
        }
    }

    private ResponseEntity<OrderResponse> sellOrder(Optional<UserHolding> userHoldingOptional, FundScript fundScript, FundNav fundNav, OrderView orderView) {
        OrderResponse orderResponse = new OrderResponse();
        if (userHoldingOptional.isEmpty() || userHoldingOptional.get().getUnits().compareTo(orderView.getUnits()) <= 0) {
            // Bad request - If insufficient units on sell request
            orderResponse.setCode(INSUFFICIENT_UNITS_USER_CODE);
            orderResponse.setMessage(INSUFFICIENT_UNITS_USER);
            return new ResponseEntity<>(orderResponse, HttpStatus.BAD_REQUEST);
        }
        UserHolding userHolding = userHoldingOptional.get();
        // Reduce units count in userHolding table
        userHolding.setUnits(userHolding.getUnits().subtract(orderView.getUnits()));
        // Reduce totalValue in userHolding table
        BigDecimal unitValue = fundNav.getNav().multiply(orderView.getUnits());
        userHolding.setTotalValue(userHolding.getTotalValue().subtract(unitValue));

        // Add totalUnit count from fundScript table
        fundScript.setTotalUnits(fundScript.getTotalUnits().add(orderView.getUnits()));
        userHoldingRepository.save(userHolding);
        fundRepository.save(fundScript);
        orderResponse.setCode(ORDER_COMPLETED_CODE);
        orderResponse.setMessage(ORDER_COMPLETED);

        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    private ResponseEntity<OrderResponse> buyOrder(Optional<UserHolding> userHoldingOptional, FundScript fundScript, FundNav fundNav, User user, OrderView orderView) {
        OrderResponse orderResponse = new OrderResponse();
        UserHolding userHolding;
        if (userHoldingOptional.isPresent()) {
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
            BigDecimal unitValue = fundNav.getNav().multiply(orderView.getUnits());
            userHolding.setTotalValue(userHolding.getTotalValue().add(unitValue));
            // Reduce totalUnit count from fundScript table
            fundScript.setTotalUnits(fundScript.getTotalUnits().subtract(orderView.getUnits()));
            orderResponse.setTotalValue(unitValue);
        } else {
            // 1st time buy order creates record in UserHolding.
            userHolding = new UserHolding();
            userHolding.setUser(user);
            userHolding.setFund(fundScript);
            userHolding.setUnits(orderView.getUnits());
            BigDecimal unitValue = fundNav.getNav().multiply(orderView.getUnits());
            userHolding.setTotalValue(unitValue);
            // Reduce totalUnit count from fundScript table
            fundScript.setTotalUnits(fundScript.getTotalUnits().subtract(orderView.getUnits()));
            orderResponse.setTotalValue(unitValue);
        }
        userHoldingRepository.save(userHolding);
        fundRepository.save(fundScript);

        orderResponse.setCode(ORDER_COMPLETED_CODE);
        orderResponse.setMessage(ORDER_COMPLETED);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

}
