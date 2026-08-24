package com.inditex.prices.price.infrastructure.adapter.out.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PriceJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "brand_id", nullable = false)
    private long brandId;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "price_list", nullable = false)
    private long priceList;

    @Column(name = "product_id", nullable = false)
    private long productId;

    @Column(name = "priority", nullable = false)
    private int priority;

    @Column(
        name = "price",
        nullable = false,
        precision = 19,
        scale = 4
    )
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;
}
