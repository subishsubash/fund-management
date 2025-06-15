package com.subash.fund.management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Entity class for funds_scripts table
 */
@Entity
@Table(name = "funds_scripts")
@Data
public class FundScript {

    @Id
    @Column(name = "fund_id", unique = true, updatable = false, nullable = false)
    private String fundId;

    @Column(name = "fund_name", nullable = false)
    private String fundName;

    @Column(name = "total_units", nullable = false)
    private BigDecimal totalUnits;

}
