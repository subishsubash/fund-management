package com.subash.fund.management.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity class representing the Net Asset Value (NAV) details of a fund.
 * <p>
 * This class is mapped to the {@code fund_navs} table in the database.
 * Each record corresponds to the NAV of a specific fund on a specific date.
 * </p>
 *
 * <p>
 * Unique constraint ensures that each fund has only one NAV entry per date.
 * </p>
 * <p>
 * Example: NAV of fund ABC on 2024-06-15 is ₹123.45.
 *
 * @see FundScript
 */
@Entity
@Table(name = "fund_navs", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"fund_id", "navDate"})
})
@Data
public class FundNav {


    /**
     * Primary key identifier for the NAV entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /**
     * Reference to the associated {@link FundScript} (fund entity).
     */
    @ManyToOne
    @JoinColumn(name = "fund_id", nullable = false)
    private FundScript fundId;

    /**
     * Net Asset Value (NAV) of the fund on the given date.
     */
    @Column(name = "nav", nullable = false)
    private BigDecimal nav;

    /**
     * Date on which the NAV is recorded.
     */
    @Column(name = "nav_date", nullable = false)
    private LocalDate navDate;

}
