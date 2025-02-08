-- changeset dzakirin:0001-populate-test-data.sql

-- Insert Sample Customers
INSERT INTO customers (id, first_name, last_name, email) VALUES
    (gen_random_uuid(), 'John', 'Doe', 'john.doe@example.com'),
    (gen_random_uuid(), 'Jane', 'Smith', 'jane.smith@example.com');

-- Insert Sample Products
INSERT INTO products (id, title, price, stock) VALUES
    (gen_random_uuid(), 'Java Programming Book', 20.01, 100),
    (gen_random_uuid(), 'Spring Boot Essentials', 120.01, 50);

-- Insert Sample Orders
INSERT INTO orders (id, customer_id, order_date) VALUES
    (gen_random_uuid(), (SELECT id FROM customers WHERE email='john.doe@example.com'), NOW()),
    (gen_random_uuid(), (SELECT id FROM customers WHERE email='jane.smith@example.com'), NOW());

-- Insert Sample Order-Products (Many-to-Many)
INSERT INTO order_products (id, order_id, product_id, quantity) VALUES
    (gen_random_uuid(),
     (SELECT id FROM orders WHERE customer_id = (SELECT id FROM customers WHERE email='john.doe@example.com') LIMIT 1),
     (SELECT id FROM products WHERE title = 'Java Programming Book'),
     2
    ),
    (gen_random_uuid(),
     (SELECT id FROM orders WHERE customer_id = (SELECT id FROM customers WHERE email='jane.smith@example.com') LIMIT 1),
     (SELECT id FROM products WHERE title = 'Spring Boot Essentials'),
     1
    );
