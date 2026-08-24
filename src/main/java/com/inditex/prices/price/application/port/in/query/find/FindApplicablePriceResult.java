package com.inditex.prices.price.application.port.in.query.find;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import com.inditex.prices.price.domain.model.Price;

public record FindApplicablePriceResult(
    long productId,
    long brandId,
    long priceList,
    LocalDateTime startDate,
    LocalDateTime endDate,
    BigDecimal amount,
    String currency
) {

    public static FindApplicablePriceResult from(Price price) {
        Objects.requireNonNull(price, "Price cannot be null");

        return new FindApplicablePriceResult(
            price.productId(),
            price.brandId(),
            price.priceList(),
            price.startDate(),
            price.endDate(),
            price.finalPrice().amount(),
            price.finalPrice().currency().getCurrencyCode()
        );
    }
}
