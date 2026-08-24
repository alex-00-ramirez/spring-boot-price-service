package com.inditex.prices.price.domain.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.inditex.prices.price.domain.exception.InvalidPriceException;
import com.inditex.prices.price.domain.valueobject.Money;

class PriceTest {

    private static final LocalDateTime START_DATE =
        LocalDateTime.of(2020, 6, 14, 15, 0);

    private static final LocalDateTime END_DATE =
        LocalDateTime.of(2020, 6, 14, 18, 30);

    @Test
    void shouldApplyAtBothPeriodBoundaries() {
        Price price = validPrice();

        assertTrue(price.appliesAt(START_DATE));
        assertTrue(price.appliesAt(END_DATE));
    }

    @Test
    void shouldApplyInsidePeriod() {
        Price price = validPrice();

        assertTrue(
            price.appliesAt(
                LocalDateTime.of(2020, 6, 14, 16, 0)
            )
        );
    }

    @Test
    void shouldNotApplyOutsidePeriod() {
        Price price = validPrice();

        assertFalse(
            price.appliesAt(START_DATE.minusNanos(1))
        );
        assertFalse(
            price.appliesAt(END_DATE.plusNanos(1))
        );
    }

    @Test
    void shouldRejectInvalidDateRange() {
        assertThrows(
            InvalidPriceException.class,
            () -> new Price(
                1,
                35455,
                2,
                END_DATE,
                START_DATE,
                1,
                validMoney()
            )
        );
    }

    @Test
    void shouldRejectNegativePriority() {
        assertThrows(
            InvalidPriceException.class,
            () -> new Price(
                1,
                35455,
                2,
                START_DATE,
                END_DATE,
                -1,
                validMoney()
            )
        );
    }

    @Test
    void shouldRejectNullApplicationDate() {
        Price price = validPrice();

        assertThrows(
            InvalidPriceException.class,
            () -> price.appliesAt(null)
        );
    }

    private Price validPrice() {
        return new Price(
            1,
            35455,
            2,
            START_DATE,
            END_DATE,
            1,
            validMoney()
        );
    }

    private Money validMoney() {
        return Money.of(
            new BigDecimal("25.45"),
            "EUR"
        );
    }
}