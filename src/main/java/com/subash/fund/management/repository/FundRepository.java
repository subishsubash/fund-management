package com.subash.fund.management.repository;

import com.subash.fund.management.model.FundScript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FundRepository extends JpaRepository<FundScript, String> {
}
