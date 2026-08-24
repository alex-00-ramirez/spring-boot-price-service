package com.inditex.prices.price.infrastructure.adapter.in.rest.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.inditex.prices.price.application.port.in.query.find.FindApplicablePriceQuery;
import com.inditex.prices.price.application.port.in.query.find.FindApplicablePriceResult;
import com.inditex.prices.price.infrastructure.adapter.in.rest.dto.request.FindApplicablePriceRequest;
import com.inditex.prices.price.infrastructure.adapter.in.rest.dto.response.FindApplicablePriceResponse;

@Component
public final class PriceRestMapper {

    public FindApplicablePriceQuery toQuery(
        FindApplicablePriceRequest request
    ) {
        Objects.requireNonNull(
            request,
            "Find applicable price request cannot be null"
        );

        return new FindApplicablePriceQuery(
            request.applicationDate(),
            request.productId(),
            request.brandId()
        );
    }

    public FindApplicablePriceResponse toResponse(
        FindApplicablePriceResult result
    ) {
        Objects.requireNonNull(
            result,
            "Find applicable price result cannot be null"
        );

        return new FindApplicablePriceResponse(
            result.productId(),
            result.brandId(),
            result.priceList(),
            result.startDate(),
            result.endDate(),
            result.amount(),
            result.currency()
        );
    }
}