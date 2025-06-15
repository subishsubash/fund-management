package com.subash.fund.management.controller;


import com.subash.fund.management.model.FundNavView;
import com.subash.fund.management.model.FundResponse;
import com.subash.fund.management.model.FundView;
import com.subash.fund.management.service.FundService;
import com.subash.fund.management.util.Constants;
import com.subash.fund.management.util.GenericLogger;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.subash.fund.management.util.Constants.COMMA;
import static com.subash.fund.management.util.Constants.LOG_MESSAGE;

/**
 * REST controller for managing fund-related operations.
 * <p>
 * This controller provides endpoints for registering a new fund and updating NAV for an existing fund.
 * It uses {@link FundService} to perform the actual business logic and {@link GenericLogger} for standardized logging.
 * </p>
 */
@RestController
@RequestMapping("/v1/api")
public class FundController {

    private static final Logger logger = LogManager.getLogger(FundController.class);

    private final FundService fundService;
    private final GenericLogger genericLogger;


    /**
     * Constructs a new {@code FundController} with the required service and logger components.
     *
     * @param fundService   service handling fund operations
     * @param genericLogger utility for standardized logging
     */
    public FundController(FundService fundService, GenericLogger genericLogger) {
        this.fundService = fundService;
        this.genericLogger = genericLogger;
    }

    /**
     * Registers a new fund based on the provided fund details.
     *
     * @param fundView the fund details submitted in the request body
     * @return {@link ResponseEntity} containing the created fund details and HTTP status
     * @throws Exception if fund creation fails
     * @apiNote Endpoint: {@code POST /v1/api/funds}
     */
    @PostMapping("/funds")
    public ResponseEntity<FundResponse> createFund(@Valid @RequestBody FundView fundView) throws Exception {
        String uuid = GenericLogger.getUUID();
        logger.info(uuid + COMMA + LOG_MESSAGE + "Request received to fund registration");
        //Log request
        genericLogger.logRequest(logger, uuid, Constants.CREATE_FUND, Constants.POST_METHOD, fundView);
        ResponseEntity<FundResponse> fundResponse = fundService.createFund(uuid, fundView);
        //Log response
        genericLogger.logResponse(logger, uuid, HttpStatus.OK.name(), fundResponse);
        logger.info(uuid + COMMA + LOG_MESSAGE + "Fund registration request completed");
        return fundResponse;
    }

    /**
     * Updates the NAV (Net Asset Value) of an existing fund.
     *
     * @param fundId      the ID of the fund to be updated
     * @param fundNavView the NAV details submitted in the request body
     * @return {@link ResponseEntity} containing the updated fund details and HTTP status
     * @throws Exception if fund update fails
     * @apiNote Endpoint: {@code PUT /v1/api/funds/{fundId}}
     */
    @PutMapping("/funds/{fundId}")
    public ResponseEntity<FundResponse> updateFund(@Valid @PathVariable("fundId") String fundId, @Valid @RequestBody FundNavView fundNavView) throws Exception {
        String uuid = GenericLogger.getUUID();
        logger.info(uuid + COMMA + LOG_MESSAGE + "Request received to update fund");
        //Log request
        genericLogger.logRequest(logger, uuid, Constants.UPDATE_FUND, Constants.PUT_METHOD, fundNavView);
        ResponseEntity<FundResponse> fundResponse = fundService.updateFund(uuid, fundId, fundNavView);
        //Log response
        genericLogger.logResponse(logger, uuid, HttpStatus.OK.name(), fundResponse);
        logger.info(uuid + COMMA + LOG_MESSAGE + "Fund update request completed");
        return fundResponse;
    }

}
