package entity;

public class CarLoan extends Loan {
    private String carModel;
    private int carValue;

    public CarLoan() {}

    public CarLoan(int loanId, Customer customer, double principalAmount, double interestRate, int loanTerm, String loanStatus, String carModel, int carValue) {
        super(loanId, customer, principalAmount, interestRate, loanTerm, "CarLoan", loanStatus);
        this.carModel = carModel;
        this.carValue = carValue;
    }

    public String getCarModel() { return carModel; }
    public void setCarModel(String carModel) { this.carModel = carModel; }

    public int getCarValue() { return carValue; }
    public void setCarValue(int carValue) { this.carValue = carValue; }

    public void printCarLoanDetails() {
        printLoanDetails();
        System.out.println("Car Model: " + carModel);
        System.out.println("Car Value: " + carValue);
    }
}
