package com.inditex.prices.price.application.port.in.query.find;

@FunctionalInterface
public interface FindApplicablePriceUseCase {

    FindApplicablePriceResult execute(
        FindApplicablePriceQuery query
    );
}
