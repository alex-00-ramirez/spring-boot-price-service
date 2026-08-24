package com.inditex.prices.price.domain.exception;

public final class InvalidMoneyException extends RuntimeException {

    public InvalidMoneyException(String message) {
        super(message);
    }

    public InvalidMoneyException(String message, Throwable cause) {
        super(message, cause);
    }
}
