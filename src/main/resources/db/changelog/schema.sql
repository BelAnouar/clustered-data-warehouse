CREATE TABLE IF NOT EXISTS deals (
                                     deal_unique_id VARCHAR(50) PRIMARY KEY,
    from_currency_iso_code VARCHAR(3) NOT NULL,
    to_currency_iso_code VARCHAR(3) NOT NULL,
    deal_timestamp TIMESTAMP NOT NULL,
    deal_amount DECIMAL(18,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );