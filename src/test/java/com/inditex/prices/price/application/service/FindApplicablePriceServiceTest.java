package com.inditex.prices.price.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.inditex.prices.price.application.exception.ApplicablePriceNotFoundException;
import com.inditex.prices.price.application.port.in.query.find.FindApplicablePriceQuery;
import com.inditex.prices.price.application.port.in.query.find.FindApplicablePriceResult;
import com.inditex.prices.price.application.port.out.PriceRepository;
import com.inditex.prices.price.domain.model.Price;
import com.inditex.prices.price.domain.valueobject.Money;

class FindApplicablePriceServiceTest {

    private static final LocalDateTime APPLICATION_DATE =
        LocalDateTime.of(2020, 6, 14, 16, 0);

    @Test
    void shouldReturnApplicablePrice() {
        FindApplicablePriceQuery query =
            new FindApplicablePriceQuery(
                APPLICATION_DATE,
                35455,
                1
            );

        Price price = new Price(
            1,
            35455,
            2,
            LocalDateTime.of(2020, 6, 14, 15, 0),
            LocalDateTime.of(2020, 6, 14, 18, 30),
            1,
            Money.of(
                new BigDecimal("25.45"),
                "EUR"
            )
        );

        PriceRepository priceRepository = (
            applicationDate,
            productId,
            brandId
        ) -> {
            assertEquals(APPLICATION_DATE, applicationDate);
            assertEquals(35455, productId);
            assertEquals(1, brandId);

            return Optional.of(price);
        };

        FindApplicablePriceService service =
            new FindApplicablePriceService(priceRepository);

        FindApplicablePriceResult result =
            service.execute(query);

        assertEquals(35455, result.productId());
        assertEquals(1, result.brandId());
        assertEquals(2, result.priceList());
        assertEquals(
            new BigDecimal("25.45"),
            result.amount()
        );
        assertEquals("EUR", result.currency());
    }

    @Test
    void shouldThrowExceptionWhenNoApplicablePriceExists() {
        FindApplicablePriceQuery query =
            new FindApplicablePriceQuery(
                APPLICATION_DATE,
                99999,
                1
            );

        PriceRepository priceRepository = (
            applicationDate,
            productId,
            brandId
        ) -> {
            assertEquals(APPLICATION_DATE, applicationDate);
            assertEquals(99999, productId);
            assertEquals(1, brandId);

            return Optional.empty();
        };

        FindApplicablePriceService service =
            new FindApplicablePriceService(priceRepository);

        assertThrows(
            ApplicablePriceNotFoundException.class,
            () -> service.execute(query)
        );
    }
}
