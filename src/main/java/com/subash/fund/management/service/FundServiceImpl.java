package com.subash.fund.management.service;

import com.subash.fund.management.mapper.FundMapper;
import com.subash.fund.management.model.FundNav;
import com.subash.fund.management.model.FundResponse;
import com.subash.fund.management.model.FundScript;
import com.subash.fund.management.model.FundView;
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

@Service
public class FundServiceImpl implements FundService {

    private static final Logger logger = LogManager.getLogger(FundServiceImpl.class);

    private final FundRepository fundRepository;

    private final FundNavRepository fundNavRepository;
    private final GenericLogger genericLogger;


    public FundServiceImpl(FundRepository fundRepository, FundNavRepository fundNavRepository, GenericLogger genericLogger) {
        this.fundRepository = fundRepository;
        this.fundNavRepository = fundNavRepository;
        this.genericLogger = genericLogger;
    }

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
                fundNav.setNav(new BigDecimal(fundView.getNav()));
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
}
