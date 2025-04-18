package util;

import exception.InvalidInputException;

public class ValidationService {
    public static void validateNotNull(Object input, String message) {
        if (input == null || (input instanceof String && ((String) input).isBlank())) {
            throw new InvalidInputException(message);
        }
    }

    public static void validatePositiveNumber(double value, String message) {
        if (value <= 0) {
            throw new InvalidInputException(message);
        }
    }
}
