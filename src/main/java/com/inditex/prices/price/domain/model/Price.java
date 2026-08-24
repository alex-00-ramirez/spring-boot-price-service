package com.inditex.prices.price.domain.model;

import java.time.LocalDateTime;

import com.inditex.prices.price.domain.exception.InvalidPriceException;
import com.inditex.prices.price.domain.valueobject.Money;

public record Price(
    long brandId,
    long productId,
    long priceList,
    LocalDateTime startDate,
    LocalDateTime endDate,
    int priority,
    Money finalPrice
) {

    public Price {
        if (brandId <= 0) {
            throw new InvalidPriceException(
                "Brand identifier must be greater than zero"
            );
        }

        if (productId <= 0) {
            throw new InvalidPriceException(
                "Product identifier must be greater than zero"
            );
        }

        if (priceList <= 0) {
            throw new InvalidPriceException(
                "Price list identifier must be greater than zero"
            );
        }

        if (startDate == null) {
            throw new InvalidPriceException("Start date is required");
        }

        if (endDate == null) {
            throw new InvalidPriceException("End date is required");
        }

        if (startDate.isAfter(endDate)) {
            throw new InvalidPriceException(
                "Start date cannot be after end date"
            );
        }

        if (priority < 0) {
            throw new InvalidPriceException(
                "Priority cannot be negative"
            );
        }

        if (finalPrice == null) {
            throw new InvalidPriceException("Final price is required");
        }
    }

    public boolean appliesAt(LocalDateTime applicationDate) {
        if (applicationDate == null) {
            throw new InvalidPriceException(
                "Application date is required"
            );
        }

        return !applicationDate.isBefore(startDate)
            && !applicationDate.isAfter(endDate);
    }
}
