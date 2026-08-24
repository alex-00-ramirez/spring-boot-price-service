package com.inditex.prices.price.infrastructure.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Repository;

import com.inditex.prices.price.application.port.out.PriceRepository;
import com.inditex.prices.price.domain.model.Price;
import com.inditex.prices.price.infrastructure.adapter.out.persistence.mapper.PricePersistenceMapper;
import com.inditex.prices.price.infrastructure.adapter.out.persistence.repository.PriceJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PricePersistenceAdapter implements PriceRepository {

    private final PriceJpaRepository priceJpaRepository;
    private final PricePersistenceMapper pricePersistenceMapper;

    @Override
    public Optional<Price> findApplicablePrice(
        LocalDateTime applicationDate,
        long productId,
        long brandId
    ) {
        return priceJpaRepository
            .findApplicablePrices(
                applicationDate,
                productId,
                brandId,
                Limit.of(1)
            )
            .stream()
            .findFirst()
            .map(pricePersistenceMapper::toDomain);
    }

}
