﻿USE master;

ALTER DATABASE ExpenseManager SET SINGLE_USER WITH ROLLBACK IMMEDIATE;

IF EXISTS (SELECT * FROM sys.databases WHERE name = 'ExpenseManager')
BEGIN
    DROP DATABASE ExpenseManager;
END;

CREATE DATABASE ExpenseManager;
USE ExpenseManager;

-- Bảng Users
CREATE TABLE Users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    createdTime DATETIME DEFAULT GETDATE(),
    isActive BIT DEFAULT 1
);

-- Bảng Categories
CREATE TABLE Categories (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(10) CHECK (type IN ('income', 'expense')) NOT NULL
);

-- Bảng Transactions
CREATE TABLE Transactions (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    category_id INT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    date DATE NOT NULL,
    note TEXT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES Categories(id) ON DELETE CASCADE
);

-- Bảng SavingsGoals (Giữ lại cột current_amount)
CREATE TABLE SavingsGoals (
    goal_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    goal_name NVARCHAR(100) NOT NULL,
    target_amount DECIMAL(15,2) NOT NULL,
    current_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    deadline DATE NOT NULL, 
	status VARCHAR(20) NOT NULL DEFAULT 'In Progress',
	completed_date DATE NULL,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE SharedData (
    id INT IDENTITY(1,1) PRIMARY KEY,
    owner_id INT NOT NULL,        -- Người chia sẻ dữ liệu
    shared_with_id INT NOT NULL,  -- Người nhận dữ liệu
    income DECIMAL(15,2) NOT NULL,
    expense DECIMAL(15,2) NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES Users(id),
    FOREIGN KEY (shared_with_id) REFERENCES Users(id)
);
-- Thêm dữ liệu vào bảng Users
INSERT INTO Users (username, email, password, balance, createdTime, isActive) 
VALUES 
('john_doe', 'john@example.com', 'password123', 0, GETDATE(), 1),
('jane_smith', 'jane@example.com', 'securepass', 0, GETDATE(), 1),
('mike_lee', 'mike@example.com', 'mikepass', 0, GETDATE(), 1),
('pgk0709', 'phamgiakhanh0709@gmail.com', '123', 0, GETDATE(), 1);

-- Thêm dữ liệu vào bảng Categories
INSERT INTO Categories (name, type) VALUES 
('Salary', 'income'),
('Bonus', 'income'),
('Investment', 'income'),
('Food & Drinks', 'expense'),
('Shopping', 'expense'),
('Bills & Utilities', 'expense'),
('Entertainment', 'expense'),
('Health', 'expense'),
('Transport', 'expense'),
('Saving Goal', 'income'),
('Saving Goal', 'expense');

-- Thêm dữ liệu vào bảng Transactions
INSERT INTO Transactions (user_id, category_id, amount, date, note) 
VALUES 
(1, 1, 2000.00, '2025-03-01', 'Monthly Salary'),
(1, 4, 150.00, '2025-03-02', 'Lunch at restaurant'),
(1, 7, 100.00, '2025-03-05', 'Movie tickets'),
(2, 2, 500.00, '2025-03-03', 'Freelance payment'),
(2, 5, 200.00, '2025-03-04', 'New clothes'),
(3, 6, 50.00, '2025-03-05', 'Electricity bill');

-- Thêm dữ liệu vào bảng SavingsGoals (Giữ current_amount)
INSERT INTO SavingsGoals (user_id, goal_name, target_amount, current_amount, deadline) 
VALUES 
(1, 'Buy a new laptop', 1500.00, 200.00, '2025-12-31'),
(2, 'Vacation trip', 3000.00, 500.00, '2026-06-30'),
(3, 'Emergency Fund', 5000.00, 1000.00, '2025-10-01');


SELECT * FROM Users
SELECT * FROM Categories
SELECT * FROM Transactions
SELECT * FROM SavingsGoals
SELECT * FROM SharedData
DELETE FROM SharedData WHERE id = 7

SELECT u.username, s.income, s.expense 
FROM SharedData s 
JOIN Users u ON s.user_id = u.id 
WHERE s.shared_with_id = ?;
SELECT * FROM Users WHERE 
INSERT INTO Users (username, email, password) VALUES (?,?,?)
SELECT DISTINCT YEAR(date) FROM Transactions "
                + "WHERE user_id = 4 ORDER BY YEAR(date) DESC
				SELECT DISTINCT YEAR(date) FROM Transactions WHERE user_id = 4 ORDER BY YEAR(date) DESC
INSERT INTO Transactions (user_id, category_id, amount, date, note) 
VALUES (4, 1, 20000, '2025-03-01', 'ABC'),
(4, 3, 2222, '2024-03-01', 'ABC')


INSERT INTO Transactions (user_id, category_id, amount, date, note) 
VALUES
(4, 4, 1000, '2025-07-01', '232'),
(4, 6, 1000, '2025-03-01', 'ABC23223DE')
INSERT INTO Transactions (user_id, category_id, amount, date, note) 
VALUES
(4, 7, 1000, '2024-07-01', '2331')

select * from Transactions


SELECT DISTINCT MONTH(date) FROM Transactions WHERE user_id = 4 AND YEAR(date) = '2025'

SELECT 
    MONTH(date) AS month, 
    SUM(amount) AS total_income
FROM Transactions
WHERE user_id = 4 
    AND YEAR(date) = '2025' 
    AND type = 'income' 
GROUP BY MONTH(date)


ORDER BY MONTH(date);

SELECT 
    c.name AS category_name,
    t.type,
    SUM(t.amount) AS total_amount
FROM Transactions t
JOIN Categories c ON t.category_id = c.id
WHERE t.user_id = 4 AND YEAR(t.date) = '2024' AND t.type = 'expense'
GROUP BY t.user_id, c.name, t.type
ORDER BY t.type, total_amount DESC;

SELECT * FROM Categories WHERE type = 'income'
SELECT * FROM Transactions WHERE user_id = 4;
SELECT 
    t.id,
    t.user_id,
    t.category_id,
    c.name AS category_name,
    c.type AS transaction_type,
    t.amount,
    t.date AS transaction_date,
    t.note AS description
FROM Transactions t
JOIN Categories c ON
t.category_id = c.id
WHERE t.user_id = 4;

SELECT * FROM Categories WHERE id = 4

INSERT INTO Transactions (user_id, category_id, amount, date, note) 
VALUES
(4, 5, 1000, '2025-07-01', '232'),
(4, 6, 1000, '2025-03-01', 'ABC23223DE')

SELECT * FROM Transactions t JOIN Categories c ON t.category_id = c.id WHERE c.id = ? AND user_id = ?

DELETE FROM Transactions WHERE id = 20 AND user_id = 4

SELECT * FROM Users WHERE username = 'pgk0709'

UPDATE Users SET balance = 0 WHERE id = 4;

SELECT * FROM Users

SELECT * FROM Transactions
SELECT * FROM Categories

UPDATE Users SET  balance = 0.00 WHERE id = 4;
SHOW VARIABLES LIKE 'character_set_%';
ALTER TABLE Transactions ALTER COLUMN note NVARCHAR(255);

SELECT * FROM SavingsGoals WHERE user_id = 4;

INSERT INTO SavingsGoals (goal_name, target_amount, current_amount, deadline) 
VALUES (?, ?, ?, ?)

SELECT current_amount, target_amount FROM SavingsGoals WHERE goal_id = 10;

SELECT * FROM SavingsGoals WHERE user_id = 4;
DELETE FROM Transactions WHERE user_id = 4;