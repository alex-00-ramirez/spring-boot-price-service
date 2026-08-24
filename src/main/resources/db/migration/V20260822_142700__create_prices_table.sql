CREATE TABLE prices (
    id BIGINT NOT NULL,
    brand_id BIGINT NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    price_list BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    priority INTEGER NOT NULL,
    price DECIMAL(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,

    CONSTRAINT pk_prices
        PRIMARY KEY (id),

    CONSTRAINT ck_prices_brand_id_positive
        CHECK (brand_id > 0),

    CONSTRAINT ck_prices_product_id_positive
        CHECK (product_id > 0),

    CONSTRAINT ck_prices_price_list_positive
        CHECK (price_list > 0),

    CONSTRAINT ck_prices_valid_date_range
        CHECK (start_date <= end_date),

    CONSTRAINT ck_prices_priority_not_negative
        CHECK (priority >= 0),

    CONSTRAINT ck_prices_price_positive
        CHECK (price > 0)
);

CREATE INDEX idx_prices_applicable
    ON prices (
        brand_id,
        product_id,
        start_date,
        end_date,
        priority DESC
    );