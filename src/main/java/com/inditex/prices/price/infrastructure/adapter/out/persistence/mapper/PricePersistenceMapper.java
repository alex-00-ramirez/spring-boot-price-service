package com.inditex.prices.price.infrastructure.adapter.out.persistence.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.inditex.prices.price.domain.model.Price;
import com.inditex.prices.price.domain.valueobject.Money;
import com.inditex.prices.price.infrastructure.adapter.out.persistence.entity.PriceJpaEntity;

@Component
public final class PricePersistenceMapper {

    public Price toDomain(PriceJpaEntity entity) {
        Objects.requireNonNull(
            entity,
            "Price JPA entity cannot be null"
        );

        return new Price(
            entity.getBrandId(),
            entity.getProductId(),
            entity.getPriceList(),
            entity.getStartDate(),
            entity.getEndDate(),
            entity.getPriority(),
            Money.of(
                entity.getAmount(),
                entity.getCurrency()
            )
        );
    }
}