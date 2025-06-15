package com.subash.fund.management.repository;

import com.subash.fund.management.model.FundScript;
import com.subash.fund.management.model.User;
import com.subash.fund.management.model.UserHolding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserHoldingRepository extends JpaRepository<UserHolding, Long> {
    Optional<UserHolding> findByUserAndFund(User user, FundScript fund);

}
