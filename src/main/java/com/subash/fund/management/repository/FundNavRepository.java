package com.subash.fund.management.repository;

import com.subash.fund.management.model.FundNav;
import com.subash.fund.management.model.FundScript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;


@Repository
public interface FundNavRepository extends JpaRepository<FundNav, Long> {

    public FundNav findByFundId(FundScript fundScript);

    Optional<FundNav> findByFundIdAndNavDate(FundScript fundId, LocalDate date);
}
