package com.inditex.prices.price.domain.exception;

public final class InvalidPriceException extends RuntimeException {

    public InvalidPriceException(String message) {
        super(message);
    }

    public InvalidPriceException(
        String message,
        Throwable cause
    ) {
        super(message, cause);
    }
}
