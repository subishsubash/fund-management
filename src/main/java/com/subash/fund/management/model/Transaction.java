package com.subash.fund.management.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class for transactions table
 */
@Entity
@Table(name = "transactions")
@Data
public class Transaction {

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

    @Column(name = "type", nullable = false)
    private String type; // 'BUY' or 'REDEEM'

    @Column(name = "units", nullable = false)
    private BigDecimal units;

    @Column(name = "nav", nullable = false)
    private BigDecimal nav;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();
}
