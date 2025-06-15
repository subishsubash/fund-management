package com.subash.fund.management.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Entity class for user_holdings table
 */
@Entity
@Table(name = "user_holdings", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "fund_id"})
})
@Data
public class UserHolding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "fund_id", nullable = false)
    private FundScript fund;

    @Column(name = "units", nullable = false)
    private BigDecimal units;

    @Column(name = "total_value", nullable = false)
    private BigDecimal totalValue;

}
