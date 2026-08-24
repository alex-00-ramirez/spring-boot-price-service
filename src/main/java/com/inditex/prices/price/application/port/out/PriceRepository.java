package com.inditex.prices.price.application.port.out;

import java.time.LocalDateTime;
import java.util.Optional;

import com.inditex.prices.price.domain.model.Price;

public interface PriceRepository {

    Optional<Price> findApplicablePrice(
        LocalDateTime applicationDate,
        long productId,
        long brandId
    );
}
