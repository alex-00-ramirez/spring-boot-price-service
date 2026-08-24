package com.inditex.prices.price.infrastructure.adapter.in.rest.dto.request;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FindApplicablePriceRequest(

    @NotNull(
        message = "Application date is required"
    )
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime applicationDate,

    @Positive(
        message = "Product identifier must be greater than zero"
    )
    long productId,

    @Positive(
        message = "Brand identifier must be greater than zero"
    )
    long brandId
) {
}