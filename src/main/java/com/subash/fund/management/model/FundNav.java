package com.subash.fund.management.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity class for fund_navs table
 */
@Entity
@Table(name = "fund_navs", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"fund_id", "navDate"})
})
@Data
public class FundNav {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fund_id", nullable = false)
    private FundScript fundId;

    @Column(name = "nav", nullable = false)
    private BigDecimal nav;

    @Column(name = "nav_date", nullable = false)
    private LocalDate navDate;

}
