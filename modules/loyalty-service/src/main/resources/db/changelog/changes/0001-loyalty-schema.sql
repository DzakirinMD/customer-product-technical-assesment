-- changeset dzakirin:0001-loyalty-schema

-- Loyalty Points Table (Tracks Customer's Total Points)
CREATE TABLE loyalty_points (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL UNIQUE, -- Store customer UUID (No FK to customers)
    total_points INT NOT NULL DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Loyalty Transactions Table (References `loyalty_points`)
CREATE TABLE loyalty_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    loyalty_points_id UUID NOT NULL, -- FK to `loyalty_points`
    order_id UUID,  -- Nullable for non-order transactions
    points_awarded INT NOT NULL CHECK (points_awarded >= 0),
    transaction_type VARCHAR(50) NOT NULL CHECK (transaction_type IN ('EARNED', 'REDEEMED')),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (loyalty_points_id) REFERENCES loyalty_points(id) ON DELETE CASCADE
);

-- Loyalty Rules Table (Defines How Many Points Are Given)
CREATE TABLE loyalty_rules (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    min_order_amount DECIMAL(10,2) NOT NULL CHECK (min_order_amount > 0),
    points_awarded INT NOT NULL CHECK (points_awarded > 0),
    rule_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
