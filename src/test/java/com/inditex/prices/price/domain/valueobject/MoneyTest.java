package com.inditex.prices.price.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.inditex.prices.price.domain.exception.InvalidMoneyException;

class MoneyTest {

    @Test
    void shouldCreateAndNormalizeMoney() {
        Money money = Money.of(
            new BigDecimal("25.4"),
            " eur "
        );

        assertEquals(
            new BigDecimal("25.40"),
            money.amount()
        );
        assertEquals(
            Currency.getInstance("EUR"),
            money.currency()
        );
        assertEquals("25.40 EUR", money.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-0.01"})
    void shouldRejectNonPositiveAmount(String amount) {
        assertThrows(
            InvalidMoneyException.class,
            () -> Money.of(
                new BigDecimal(amount),
                "EUR"
            )
        );
    }

    @Test
    void shouldRejectNullAmount() {
        assertThrows(
            InvalidMoneyException.class,
            () -> Money.of(null, "EUR")
        );
    }

    @Test
    void shouldRejectInvalidCurrencyCode() {
        assertThrows(
            InvalidMoneyException.class,
            () -> Money.of(
                new BigDecimal("25.45"),
                "INVALID"
            )
        );
    }

    @Test
    void shouldRejectTooManyDecimalPlaces() {
        assertThrows(
            InvalidMoneyException.class,
            () -> Money.of(
                new BigDecimal("25.451"),
                "EUR"
            )
        );
    }

    @Test
    void shouldRejectTooManyIntegerDigits() {
        assertThrows(
            InvalidMoneyException.class,
            () -> Money.of(
                new BigDecimal("1234567890123456.00"),
                "EUR"
            )
        );
    }
}