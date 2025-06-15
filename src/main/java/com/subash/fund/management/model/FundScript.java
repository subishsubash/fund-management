package com.subash.fund.management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Entity class representing a mutual fund script.
 * <p>
 * This class is mapped to the {@code funds_scripts} table in the database.
 * Each entry corresponds to a specific fund identified by a unique fund ID.
 * </p>
 *
 * <p>
 * The fund maintains details like fund name and the total number of units issued.
 * </p>
 *
 * Example: Fund ID = "HDFCEQ001", Fund Name = "HDFC Equity Fund", Units = 1,000,000
 *
 * @see FundNav
 * @see com.subash.fund.management.model.FundView
 */
@Entity
@Table(name = "funds_scripts")
@Data
public class FundScript {

    /**
     * Unique identifier for the fund.
     * Acts as the primary key for this entity.
     */
    @Id
    @Column(name = "fund_id", unique = true, updatable = false, nullable = false)
    private String fundId;

    /**
     * Name of the fund (e.g., "Axis Bluechip Fund").
     */
    @Column(name = "fund_name", nullable = false)
    private String fundName;

    /**
     * Total number of units issued by the fund.
     */
    @Column(name = "total_units", nullable = false)
    private BigDecimal totalUnits;

}
