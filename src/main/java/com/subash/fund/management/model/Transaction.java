package com.subash.fund.management.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing a transaction performed by a user on a mutual fund.
 * <p>
 * This class is mapped to the {@code transactions} table in the database. Each entry
 * records a buy or redeem operation executed by a user for a specific fund,
 * along with the NAV, units, amount, and timestamp.
 * </p>
 *
 * <p>
 * Transaction types supported: {@code BUY}, {@code REDEEM}.
 * </p>
 *
 * <p>
 * Example: User buys 100 units of HDFC Equity Fund at NAV ₹123.45 on 2024-06-15.
 * </p>
 *
 * @see User
 * @see FundScript
 * @see com.subash.fund.management.model.OrderView
 * @see com.subash.fund.management.model.OrderResponse
 */
@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    /**
     * Primary key identifier for the transaction record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /**
     * The user who initiated the transaction.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The fund associated with the transaction.
     */
    @ManyToOne
    @JoinColumn(name = "fund_id", nullable = false)
    private FundScript fund;

    /**
     * The type of transaction: either {@code BUY} or {@code REDEEM}.
     */
    @Column(name = "type", nullable = false)
    private String type; // 'BUY' or 'REDEEM'

    /**
     * The number of fund units involved in the transaction.
     */
    @Column(name = "units", nullable = false)
    private BigDecimal units;

    /**
     * Net Asset Value (NAV) of the fund at the time of transaction.
     */
    @Column(name = "nav", nullable = false)
    private BigDecimal nav;

    /**
     * Total transaction amount = units × NAV.
     */
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    /**
     * Timestamp indicating when the transaction occurred.
     * Initialized to the current date and time by default.
     */
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();
}
