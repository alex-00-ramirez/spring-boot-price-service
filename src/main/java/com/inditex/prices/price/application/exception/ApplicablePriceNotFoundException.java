package com.inditex.prices.price.application.exception;

import java.time.LocalDateTime;

public final class ApplicablePriceNotFoundException
    extends RuntimeException
{
    public ApplicablePriceNotFoundException(
        LocalDateTime applicationDate,
        long productId,
        long brandId
    ) {
        super(
            """
            No applicable price found for product %d, brand %d and date %s
            """
                .formatted(productId, brandId, applicationDate)
                .strip()
        );
    }
}
