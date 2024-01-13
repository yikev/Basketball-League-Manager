package main.Exception;

public class InvalidDateException extends Exception {
    public InvalidDateException(String val) {
        super(val + " must be of the format YYYY-MM-DD, all numbers, and must be a valid date.");
    }
}
