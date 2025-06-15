package com.subash.fund.management.service;

import com.subash.fund.management.model.FundResponse;
import com.subash.fund.management.model.FundView;
import org.springframework.http.ResponseEntity;

public interface FundService {
    ResponseEntity<FundResponse> createFund(String uuid, FundView fundView) throws Exception;
}
