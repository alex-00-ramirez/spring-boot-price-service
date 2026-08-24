package com.inditex.prices.price.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Locale;

import com.inditex.prices.price.domain.exception.InvalidMoneyException;

public record Money(
    BigDecimal amount,
    Currency currency
) {

    private static final int MAX_INTEGER_DIGITS = 15;
    private static final int MAX_FRACTION_DIGITS = 4;

    public Money {
        if (amount == null) {
            throw new InvalidMoneyException("Amount is required");
        }

        if (currency == null) {
            throw new InvalidMoneyException("Currency is required");
        }

        if (amount.signum() <= 0) {
            throw new InvalidMoneyException(
                "Amount must be greater than zero"
            );
        }

        int fractionDigits = currency.getDefaultFractionDigits();

        if (fractionDigits < 0) {
            throw new InvalidMoneyException(
                "Currency does not define a valid number of fraction digits"
            );
        }

        if (fractionDigits > MAX_FRACTION_DIGITS) {
            throw new InvalidMoneyException(
                "Currency %s requires more than %d decimal places"
                    .formatted(
                        currency.getCurrencyCode(),
                        MAX_FRACTION_DIGITS
                    )
            );
        }

        try {
            amount = amount.setScale(
                fractionDigits,
                RoundingMode.UNNECESSARY
            );

            int integerDigits = amount.precision() - amount.scale();

            if (integerDigits > MAX_INTEGER_DIGITS) {
                throw new InvalidMoneyException(
                    "Amount cannot contain more than %d integer digits"
                        .formatted(MAX_INTEGER_DIGITS)
                );
            }
        } catch (ArithmeticException exception) {
            throw new InvalidMoneyException(
                "Amount has too many decimal places for currency %s"
                    .formatted(currency.getCurrencyCode()),
                exception
            );
        }
    }

    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }

    public static Money of(BigDecimal amount, String currencyCode) {
        if (currencyCode == null || currencyCode.isBlank()) {
            throw new InvalidMoneyException("Currency code is required");
        }

        String normalizedCurrencyCode = currencyCode
            .strip()
            .toUpperCase(Locale.ROOT);

        try {
            return of(
                amount,
                Currency.getInstance(normalizedCurrencyCode)
            );
        } catch (IllegalArgumentException exception) {
            throw new InvalidMoneyException(
                "Invalid currency code: %s"
                    .formatted(normalizedCurrencyCode),
                exception
            );
        }
    }

    @Override
    public String toString() {
        return "%s %s".formatted(
            amount.toPlainString(),
            currency.getCurrencyCode()
        );
    }
}