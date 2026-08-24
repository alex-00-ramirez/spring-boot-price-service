package com.inditex.prices.price.application.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inditex.prices.price.application.exception.ApplicablePriceNotFoundException;
import com.inditex.prices.price.application.port.in.query.find.FindApplicablePriceResult;
import com.inditex.prices.price.application.port.in.query.find.FindApplicablePriceQuery;
import com.inditex.prices.price.application.port.in.query.find.FindApplicablePriceUseCase;
import com.inditex.prices.price.application.port.out.PriceRepository;
import com.inditex.prices.price.domain.model.Price;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindApplicablePriceService implements FindApplicablePriceUseCase {

    private final PriceRepository priceRepository;

    @Override
    public FindApplicablePriceResult execute(FindApplicablePriceQuery query) {

        Objects.requireNonNull(query, "Query cannot be null");

        Price applicablePrice = priceRepository
            .findApplicablePrice(
                query.applicationDate(),
                query.productId(),
                query.brandId()
            )
            .orElseThrow(
                () -> new ApplicablePriceNotFoundException(
                    query.applicationDate(),
                    query.productId(),
                    query.brandId()
                )
            );

        return FindApplicablePriceResult.from(applicablePrice);
    }
}
