package main;

import dao.LoanRepositoryImpl;
import entity.Customer;
import entity.Loan;
import exception.InvalidLoanException;

import java.util.List;
import java.util.Scanner;

public class MainModule {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LoanRepositoryImpl repo;

        try {
            repo = new LoanRepositoryImpl();

            while (true) {
                System.out.println("\n=== Loan Management System ===");
                System.out.println("1. Apply Loan");
                System.out.println("2. Calculate Interest");
                System.out.println("3. Calculate EMI");
                System.out.println("4. Get Loan Status");
                System.out.println("5. Repay Loan");
                System.out.println("6. Get All Loans");
                System.out.println("7. Get Loan by ID");
                System.out.println("8. Exit");
                System.out.print("Enter choice: ");
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        System.out.println("Enter Loan Details:");
                        System.out.print("Loan ID: ");
                        int loanId = Integer.parseInt(sc.nextLine());

                        System.out.print("Customer ID: ");
                        int customerId = Integer.parseInt(sc.nextLine());

                        System.out.print("Name: ");
                        String name = sc.nextLine();

                        System.out.print("Email: ");
                        String email = sc.nextLine();

                        System.out.print("Phone: ");
                        String phone = sc.nextLine();

                        System.out.print("Address: ");
                        String address = sc.nextLine();

                        System.out.print("Credit Score: ");
                        int score = Integer.parseInt(sc.nextLine());

                        Customer customer = new Customer(customerId, name, email, phone, address, score);

                        System.out.print("Principal Amount: ");
                        double principal = Double.parseDouble(sc.nextLine());

                        System.out.print("Interest Rate: ");
                        double rate = Double.parseDouble(sc.nextLine());

                        System.out.print("Loan Term (months): ");
                        int term = Integer.parseInt(sc.nextLine());

                        System.out.print("Loan Type (HomeLoan/CarLoan): ");
                        String type = sc.nextLine();

                        Loan loan = new Loan(loanId, customer, principal, rate, term, type, "Pending");
                        boolean applied = repo.applyLoan(loan);
                        System.out.println(applied ? "Loan Application Submitted!" : "Loan Application Cancelled.");
                        break;

                    case 2:
                        System.out.print("Enter Loan ID: ");
                        int interestId = Integer.parseInt(sc.nextLine());
                        try {
                            double interest = repo.calculateInterest(interestId);
                            System.out.println("Interest Amount: ₹" + interest);
                        } catch (InvalidLoanException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 3:
                        System.out.print("Enter Loan ID: ");
                        int emiId = Integer.parseInt(sc.nextLine());
                        try {
                            double emi = repo.calculateEMI(emiId);
                            System.out.println("Monthly EMI: ₹" + emi);
                        } catch (InvalidLoanException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 4:
                        System.out.print("Enter Loan ID: ");
                        int statusId = Integer.parseInt(sc.nextLine());
                        try {
                            String status = repo.loanStatus(statusId);
                            System.out.println("Loan Status: " + status);
                        } catch (InvalidLoanException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 5:
                        System.out.print("Enter Loan ID: ");
                        int repayId = Integer.parseInt(sc.nextLine());
                        System.out.print("Enter Amount: ");
                        double amount = Double.parseDouble(sc.nextLine());
                        try {
                            String repayMsg = repo.loanRepayment(repayId, amount);
                            System.out.println(repayMsg);
                        } catch (InvalidLoanException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 6:
                        List<Loan> loans = repo.getAllLoan();
                        for (Loan l : loans) {
                            l.printLoanDetails();
                            System.out.println("--------------------------");
                        }
                        break;

                    case 7:
                        System.out.print("Enter Loan ID: ");
                        int getId = Integer.parseInt(sc.nextLine());
                        try {
                            Loan foundLoan = repo.getLoanById(getId);
                            foundLoan.printLoanDetails();
                        } catch (InvalidLoanException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 8:
                        System.out.println("Exiting...");
                        System.exit(0);

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }

        } catch (Exception e) {
            System.out.println("System Error: " + e.getMessage());
        }
    }
}
