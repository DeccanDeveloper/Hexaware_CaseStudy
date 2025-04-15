package dao;

import entity.Loan;
import entity.Customer;
import exception.InvalidLoanException;
import util.DBConnUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoanRepositoryImpl implements ILoanRepository {

    Connection conn;

    public LoanRepositoryImpl() throws Exception {
        conn = DBConnUtil.getDBConn();
    }

    public boolean applyLoan(Loan loan) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Confirm apply loan (Yes/No): ");
        String confirm = sc.nextLine();

        if (!confirm.equalsIgnoreCase("Yes")) return false;

        String sql = "INSERT INTO Loan (loanId, customerId, principalAmount, interestRate, loanTerm, loanType, loanStatus) VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, loan.getLoanId());
        stmt.setInt(2, loan.getCustomer().getCustomerId());
        stmt.setDouble(3, loan.getPrincipalAmount());
        stmt.setDouble(4, loan.getInterestRate());
        stmt.setInt(5, loan.getLoanTerm());
        stmt.setString(6, loan.getLoanType());
        stmt.setString(7, "Pending");

        int rows = stmt.executeUpdate();
        return rows > 0;
    }

    public double calculateInterest(int loanId) throws InvalidLoanException {
        String sql = "SELECT principalAmount, interestRate, loanTerm FROM Loan WHERE loanId = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, loanId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double P = rs.getDouble("principalAmount");
                double R = rs.getDouble("interestRate");
                int N = rs.getInt("loanTerm");
                return (P * R * N) / 12;
            } else {
                throw new InvalidLoanException("Loan ID not found.");
            }
        } catch (SQLException e) {
            throw new InvalidLoanException("Database error: " + e.getMessage());
        }
    }

    public double calculateInterest(double principalAmount, double interestRate, int loanTerm) {
        return (principalAmount * interestRate * loanTerm) / 12;
    }

    public String loanStatus(int loanId) throws InvalidLoanException {
        try {
            String sql = "SELECT l.loanId, c.creditScore FROM Loan l JOIN Customer c ON l.customerId = c.customerId WHERE l.loanId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, loanId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int creditScore = rs.getInt("creditScore");
                String status = creditScore > 650 ? "Approved" : "Rejected";

                String updateSql = "UPDATE Loan SET loanStatus = ? WHERE loanId = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, status);
                updateStmt.setInt(2, loanId);
                updateStmt.executeUpdate();

                return status;
            } else {
                throw new InvalidLoanException("Loan not found.");
            }
        } catch (SQLException e) {
            throw new InvalidLoanException("Database error: " + e.getMessage());
        }
    }

    public double calculateEMI(int loanId) throws InvalidLoanException {
        String sql = "SELECT principalAmount, interestRate, loanTerm FROM Loan WHERE loanId = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, loanId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double P = rs.getDouble("principalAmount");
                double R = rs.getDouble("interestRate") / 12 / 100;
                int N = rs.getInt("loanTerm");

                return (P * R * Math.pow(1 + R, N)) / (Math.pow(1 + R, N) - 1);
            } else {
                throw new InvalidLoanException("Loan ID not found.");
            }
        } catch (SQLException e) {
            throw new InvalidLoanException("Database error: " + e.getMessage());
        }
    }

    public double calculateEMI(double principalAmount, double interestRate, int loanTerm) {
        double R = interestRate / 12 / 100;
        return (principalAmount * R * Math.pow(1 + R, loanTerm)) / (Math.pow(1 + R, loanTerm) - 1);
    }

    public String loanRepayment(int loanId, double amount) throws InvalidLoanException {
        double emi = calculateEMI(loanId);
        if (amount < emi) return "Amount too low. Cannot pay even one EMI.";

        int paidEmiCount = (int)(amount / emi);
        return "You can pay " + paidEmiCount + " EMI(s) with this amount.";
    }

    public List<Loan> getAllLoan() throws Exception {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM Loan";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Loan loan = new Loan(
                    rs.getInt("loanId"),
                    new Customer(rs.getInt("customerId"), null, null, null, null, 0),
                    rs.getDouble("principalAmount"),
                    rs.getDouble("interestRate"),
                    rs.getInt("loanTerm"),
                    rs.getString("loanType"),
                    rs.getString("loanStatus")
            );
            loans.add(loan);
        }
        return loans;
    }

    public Loan getLoanById(int loanId) throws InvalidLoanException {
        String sql = "SELECT * FROM Loan WHERE loanId = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, loanId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Loan(
                        rs.getInt("loanId"),
                        new Customer(rs.getInt("customerId"), null, null, null, null, 0),
                        rs.getDouble("principalAmount"),
                        rs.getDouble("interestRate"),
                        rs.getInt("loanTerm"),
                        rs.getString("loanType"),
                        rs.getString("loanStatus")
                );
            } else {
                throw new InvalidLoanException("Loan ID not found.");
            }
        } catch (SQLException e) {
            throw new InvalidLoanException("Database error: " + e.getMessage());
        }
    }
}
