package com.subash.fund.management.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;


/**
 * Entity class representing a user's holdings in a mutual fund.
 * <p>
 * This class is mapped to the {@code user_holdings} table in the database.
 * Each record tracks how many units of a particular fund a user owns and their total value.
 * </p>
 *
 * <p>
 * A unique constraint is enforced on the combination of {@code user_id} and {@code fund_id},
 * ensuring that a user cannot have duplicate entries for the same fund.
 * </p>
 *
 * <p>
 * Example: User123 holds 500 units of "HDFC Equity Fund" worth ₹65,000.
 * </p>
 *
 * @see User
 * @see FundScript
 * @see Transaction
 * @see com.subash.fund.management.model.OrderView
 * @see com.subash.fund.management.model.OrderResponse
 */
@Entity
@Table(name = "user_holdings", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "fund_id"})
})
@Data
public class UserHolding {

    /**
     * Primary key identifier for the user holding record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /**
     * The user who owns the fund units.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The fund in which the user has invested.
     */
    @ManyToOne
    @JoinColumn(name = "fund_id", nullable = false)
    private FundScript fund;

    /**
     * The number of units the user currently holds for the given fund.
     */
    @Column(name = "units", nullable = false)
    private BigDecimal units;

    /**
     * The total current value of the user's holding in this fund.
     * Typically calculated as {@code units × current NAV}.
     */
    @Column(name = "total_value", nullable = false)
    private BigDecimal totalValue;

}
