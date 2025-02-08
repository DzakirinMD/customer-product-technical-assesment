-- changeset dzakirin:0001-insert-loyalty-rule-min-amount

-- Insert default loyalty rule
INSERT INTO loyalty_rules (id, min_order_amount, points_awarded, rule_active, created_at)
VALUES (gen_random_uuid(), 100.00, 10, TRUE, CURRENT_TIMESTAMP);
