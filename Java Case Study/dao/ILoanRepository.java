package dao;

import entity.Loan;
import exception.InvalidLoanException;
import java.util.List;

public interface ILoanRepository {
    boolean applyLoan(Loan loan) throws Exception;

    double calculateInterest(int loanId) throws InvalidLoanException;
    double calculateInterest(double principalAmount, double interestRate, int loanTerm);

    String loanStatus(int loanId) throws InvalidLoanException;

    double calculateEMI(int loanId) throws InvalidLoanException;
    double calculateEMI(double principalAmount, double interestRate, int loanTerm);

    String loanRepayment(int loanId, double amount) throws InvalidLoanException;

    List<Loan> getAllLoan() throws Exception;

    Loan getLoanById(int loanId) throws InvalidLoanException;
}
