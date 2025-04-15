CREATE DATABASE LoanManagementSystem;
USE LoanManagementSystem
CREATE TABLE Customer (
    customerId INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    phoneNumber VARCHAR(15),
    address VARCHAR(255),
    creditScore INT
);
CREATE TABLE Loan (
    loanId INT PRIMARY KEY AUTO_INCREMENT,
    customerId INT,
    principalAmount DECIMAL(12, 2),
    interestRate DECIMAL(5, 2),
    loanTerm INT,
    loanType VARCHAR(20),
    loanStatus VARCHAR(20) DEFAULT 'Pending',
    FOREIGN KEY (customerId) REFERENCES Customer(customerId)
);
CREATE TABLE HomeLoan (
    loanId INT PRIMARY KEY,
    propertyAddress VARCHAR(255),
    propertyValue INT,
    FOREIGN KEY (loanId) REFERENCES Loan(loanId)
);
CREATE TABLE CarLoan (
    loanId INT PRIMARY KEY,
    carModel VARCHAR(100),
    carValue INT,
    FOREIGN KEY (loanId) REFERENCES Loan(loanId)
);