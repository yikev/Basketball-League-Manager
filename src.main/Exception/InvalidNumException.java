package main.Exception;

public class InvalidNumException extends Exception {
    public InvalidNumException(String val) {
        super(val + " must be a number.");
    }
}
