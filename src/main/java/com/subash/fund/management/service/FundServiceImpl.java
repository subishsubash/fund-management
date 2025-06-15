package com.subash.fund.management.service;

import com.subash.fund.management.model.FundResponse;
import com.subash.fund.management.model.FundView;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FundServiceImpl implements FundService {
    @Override
    public ResponseEntity<FundResponse> createFund(String uuid, FundView fundView) {
        return null;
    }
}
