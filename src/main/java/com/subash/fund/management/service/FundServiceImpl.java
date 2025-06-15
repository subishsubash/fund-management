package com.subash.fund.management.service;

import com.subash.fund.management.mapper.FundMapper;
import com.subash.fund.management.model.*;
import com.subash.fund.management.repository.FundNavRepository;
import com.subash.fund.management.repository.FundRepository;
import com.subash.fund.management.util.Constants;
import com.subash.fund.management.util.GenericLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static com.subash.fund.management.util.Constants.*;

/**
 * Implementation of the {@link FundService} interface for handling operations related to mutual funds.
 * <p>
 * This service handles:
 * <ul>
 *     <li>Registering new mutual funds</li>
 *     <li>Updating NAV (Net Asset Value) for existing funds</li>
 *     <li>Logging and exception handling</li>
 * </ul>
 *
 * This class uses {@link FundRepository} and {@link FundNavRepository} for data persistence,
 * and {@link FundMapper} for model transformation.
 *
 * @author Subash
 * @see FundService
 * @see FundScript
 * @see FundNav
 * @see FundView
 * @see FundNavView
 * @see FundResponse
 */
@Service
public class FundServiceImpl implements FundService {

    private static final Logger logger = LogManager.getLogger(FundServiceImpl.class);

    private final FundRepository fundRepository;

    private final FundNavRepository fundNavRepository;
    private final GenericLogger genericLogger;


    /**
     * Constructor to inject required dependencies.
     *
     * @param fundRepository     Repository for fund scripts
     * @param fundNavRepository  Repository for fund NAVs
     * @param genericLogger      Utility logger for request/response logging
     */
    public FundServiceImpl(FundRepository fundRepository, FundNavRepository fundNavRepository, GenericLogger genericLogger) {
        this.fundRepository = fundRepository;
        this.fundNavRepository = fundNavRepository;
        this.genericLogger = genericLogger;
    }

    /**
     * Registers a new mutual fund along with its initial NAV value.
     * <p>
     * If the fund already exists, it returns a response indicating the same.
     * Otherwise, it creates both the {@link FundScript} and its associated {@link FundNav}.
     *
     * @param uuid     Unique identifier for request tracking
     * @param fundView Incoming data model containing fund and NAV details
     * @return {@link ResponseEntity} with the result of the operation
     * @throws Exception if creation fails due to database or processing error
     */
    @Override
    public ResponseEntity<FundResponse> createFund(String uuid, FundView fundView) throws Exception {
        logger.info(uuid + COMMA + LOG_MESSAGE + "Processing create funds request");
        FundResponse fundResponse = new FundResponse();
        try {

            Optional<FundScript> fundOptional = fundRepository.findById(fundView.getFundId());
            if (fundOptional.isPresent()) {
                fundResponse.setCode(RECORD_EXIST_CODE);
                fundResponse.setMessage(RECORD_EXIST);
                return new ResponseEntity<>(fundResponse, HttpStatus.OK);
            } else {
                // fundScript
                FundScript fundScript = FundMapper.INSTANCE.fundViewToFundScript(fundView);
                fundResponse.setFund(FundMapper.INSTANCE.fundScriptToFundView(fundRepository.save(fundScript)));

                // FundNav
                FundNav fundNav = new FundNav();
                fundNav.setFundId(fundScript);
                fundNav.setNavDate(fundView.getNavDate());
                fundNav.setNav(fundView.getNav());
                fundNavRepository.save(fundNav);

                fundResponse.setCode(CREATE_RECORD_SUCCESS_CODE);
                fundResponse.setMessage(CREATE_RECORD_SUCCESS);
            }
        } catch (Exception e) {
            // Logger error response
            genericLogger.logResponse(logger, uuid, "ERROR", Constants.API_PROCESSED_FAILURE);
            throw new Exception(e);
        }
        logger.info(uuid + COMMA + LOG_MESSAGE + "Create fund request processed");
        return new ResponseEntity<>(fundResponse, HttpStatus.CREATED);
    }

    /**
     * Updates the NAV value for an existing mutual fund.
     * <p>
     * If the fund is not found, it returns a {@code RECORD_NOT_FOUND} response.
     *
     * @param uuid         Unique identifier for request tracking
     * @param fundId       ID of the fund to be updated
     * @param fundNavView  Incoming NAV data
     * @return {@link ResponseEntity} containing success or error message
     * @throws Exception if update fails due to database or processing error
     */
    @Override
    public ResponseEntity<FundResponse> updateFund(String uuid, String fundId, FundNavView fundNavView) throws Exception {
        logger.info(uuid + COMMA + LOG_MESSAGE + "Processing create funds request");
        FundResponse fundResponse = new FundResponse();
        try {
            Optional<FundScript> fundScriptOptional = fundRepository.findById(fundId);
            FundNav fundNav = new FundNav();

            if (fundScriptOptional.isPresent()) {
                fundNav.setFundId(fundScriptOptional.get());
                fundNav.setNavDate(fundNavView.getNavDate());
                fundNav.setNav(new BigDecimal(fundNavView.getNav()));
                fundNavRepository.save(fundNav);
                fundResponse.setCode(UPDATE_RECORD_SUCCESS_CODE);
                fundResponse.setMessage(UPDATE_RECORD_SUCCESS);
            } else {
                fundResponse.setCode(RECORD_NOT_FOUND_CODE);
                fundResponse.setMessage(RECORD_NOT_FOUND);
            }

        } catch (Exception e) {
            // Logger error response
            genericLogger.logResponse(logger, uuid, "ERROR", Constants.API_PROCESSED_FAILURE);
            throw new Exception(e);
        }
        logger.info(uuid + COMMA + LOG_MESSAGE + "Create fund request processed");
        return new ResponseEntity<>(fundResponse, HttpStatus.CREATED);
    }
}
