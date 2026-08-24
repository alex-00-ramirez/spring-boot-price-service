package com.inditex.prices.price.infrastructure.adapter.out.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inditex.prices.price.infrastructure.adapter.out.persistence.entity.PriceJpaEntity;

public interface PriceJpaRepository
    extends JpaRepository<PriceJpaEntity, Long> {

    @Query("""
        SELECT price
        FROM PriceJpaEntity price
        WHERE price.brandId = :brandId
            AND price.productId = :productId
            AND :applicationDate
                BETWEEN price.startDate AND price.endDate
        ORDER BY price.priority DESC
        """)
    List<PriceJpaEntity> findApplicablePrices(
        @Param("applicationDate")
        LocalDateTime applicationDate,
        @Param("productId")
        long productId,
        @Param("brandId")
        long brandId,
        Limit limit
    );
}