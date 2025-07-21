-- Insert sample data for Banks
INSERT INTO banks (id, name, telephone_number, email, website) VALUES
(1, 'First BankTable', '123-456-7890', 'info@firstbank.com', 'www.firstbank.com'),
(2, 'Second BankTable', '987-654-3210', 'info@secondbank.com', 'www.secondbank.com'),
(3, 'Third BankTable', '555-123-4567', 'info@thirdbank.com', 'www.thirdbank.com');

-- Insert sample data for Addresses
INSERT INTO addresses (id, street, number, city, county, postal_code, country) VALUES
(1, 'Main Street', 123, 'New York', 'NY', 10001, 'USA'),
(2, 'Broadway', 456, 'New York', 'NY', 10002, 'USA'),
(3, 'Fifth Avenue', 789, 'New York', 'NY', 10003, 'USA'),
(4, 'Park Avenue', 101, 'New York', 'NY', 10004, 'USA'),
(5, 'Lexington Avenue', 202, 'New York', 'NY', 10005, 'USA');

-- Insert sample data for Branches (linked to Banks and Addresses)
INSERT INTO branches (id, bic_code, swift_code, address_id, telephone_number, email, bank_id) VALUES
(1, 'FBNYUS33', 'FBNYUS33XXX', 1, '123-456-7891', 'branch1@firstbank.com', 1),
(2, 'FBNYUS34', 'FBNYUS34XXX', 2, '123-456-7892', 'branch2@firstbank.com', 1),
(3, 'SBNYUS33', 'SBNYUS33XXX', 3, '987-654-3211', 'branch1@secondbank.com', 2),
(4, 'TBNYUS33', 'TBNYUS33XXX', 4, '555-123-4568', 'branch1@thirdbank.com', 3);

-- Insert sample data for Clients (linked to Branches)
INSERT INTO clients (id, first_name, last_name, birth_date, cnp, telephone_number, email, created_date, branch_id) VALUES
(1, 'John', 'Doe', '1980-01-01', '1800101123456', '555-111-2222', 'john.doe@example.com', '2023-01-01', 1),
(2, 'Jane', 'Smith', '1985-05-15', '2850515123456', '555-222-3333', 'jane.smith@example.com', '2023-01-02', 1),
(3, 'Bob', 'Johnson', '1990-10-20', '1901020123456', '555-333-4444', 'bob.johnson@example.com', '2023-01-03', 2),
(4, 'Alice', 'Williams', '1975-03-25', '2750325123456', '555-444-5555', 'alice.williams@example.com', '2023-01-04', 3),
(5, 'Charlie', 'Brown', '1982-07-30', '1820730123456', '555-555-6666', 'charlie.brown@example.com', '2023-01-05', 4);

-- Insert sample data for Accounts (linked to Clients)
INSERT INTO accounts (id, account_number, type, iban_code, balance, client_id) VALUES
(1, 'ACC001', 'SAVINGS', 'US123456789012345678901234', 5000.00, 1),
(2, 'ACC002', 'CURRENT', 'US234567890123456789012345', 2500.00, 1),
(3, 'ACC003', 'MORTGAGE', 'US345678901234567890123456', 100000.00, 2),
(4, 'ACC004', 'SAVINGS', 'US456789012345678901234567', 7500.00, 3),
(5, 'ACC005', 'CURRENT', 'US567890123456789012345678', 3000.00, 4),
(6, 'ACC006', 'SAVINGS', 'US678901234567890123456789', 10000.00, 5);

-- Insert sample data for Cards (linked to Accounts)
INSERT INTO cards (id, card_number, card_holder, valid_thru, cvv_code, pin, account_id) VALUES
(1, '1234-5678-9012-3456', 'John Doe', '2025-12-31', 123, 1234, 1),
(2, '2345-6789-0123-4567', 'John Doe', '2025-12-31', 234, 2345, 2),
(3, '3456-7890-1234-5678', 'Jane Smith', '2026-06-30', 345, 3456, 3),
(4, '4567-8901-2345-6789', 'Bob Johnson', '2026-06-30', 456, 4567, 4),
(5, '5678-9012-3456-7890', 'Alice Williams', '2026-06-30', 567, 5678, 5),
(6, '6789-0123-4567-8901', 'Charlie Brown', '2026-06-30', 678, 6789, 6);

-- Insert sample data for Credentials (linked to Accounts)
INSERT INTO credentials (id, username, password, account_id) VALUES
(1, 'johndoe', 'password123', 1),
(2, 'janesmith', 'password456', 3),
(3, 'bobjohnson', 'password789', 4),
(4, 'alicewilliams', 'passwordabc', 5),
(5, 'charliebrown', 'passworddef', 6);

/*-- Insert sample Users if they don't exist already
INSERT INTO users (id, username, password, email, role)
SELECT 1, 'admin', '$2a$10$ixlPY3AAd4ty1l6E2IsQ9OFZi2ba9ZQE0bP7RFcGIWNhyFrrT3YUi', 'admin@example.com', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO users (id, username, password, email, role)
SELECT 2, 'user', '$2a$10$ixlPY3AAd4ty1l6E2IsQ9OFZi2ba9ZQE0bP7RFcGIWNhyFrrT3YUi', 'user@example.com', 'USER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user');*/