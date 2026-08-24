package com.inditex.prices.price.infrastructure.adapter.in.rest.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FindApplicablePriceResponse(
    long productId,
    long brandId,
    long priceList,
    LocalDateTime startDate,
    LocalDateTime endDate,
    BigDecimal amount,
    String currency
) {
}