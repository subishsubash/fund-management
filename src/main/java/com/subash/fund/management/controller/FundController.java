package com.subash.fund.management.controller;


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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.subash.fund.management.util.Constants.COMMA;
import static com.subash.fund.management.util.Constants.LOG_MESSAGE;

@RestController
@RequestMapping("/v1/api")
public class FundController {

    private static final Logger logger = LogManager.getLogger(FundController.class);

    private final FundService fundService;
    private final GenericLogger genericLogger;


    public FundController(FundService fundService, GenericLogger genericLogger) {

        this.fundService = fundService;
        this.genericLogger = genericLogger;
    }

    @PostMapping("/funds")
    public ResponseEntity<FundResponse> createUser(@Valid @RequestBody FundView fundView) throws Exception {
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

}
