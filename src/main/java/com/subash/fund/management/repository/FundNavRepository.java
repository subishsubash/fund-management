package com.subash.fund.management.repository;

import com.subash.fund.management.model.FundNav;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FundNavRepository extends JpaRepository<FundNav, Long> {
}
