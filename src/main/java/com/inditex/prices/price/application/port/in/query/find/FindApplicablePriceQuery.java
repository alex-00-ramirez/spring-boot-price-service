package com.inditex.prices.price.application.port.in.query.find;

import java.time.LocalDateTime;

public record FindApplicablePriceQuery(
    LocalDateTime applicationDate,
    long productId,
    long brandId
) {
}
