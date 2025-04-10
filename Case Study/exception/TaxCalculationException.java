package exception;

public class TaxCalculationException extends RuntimeException {
    public TaxCalculationException(String message) {
        super(message);
    }
}