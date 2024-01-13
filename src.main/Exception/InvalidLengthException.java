package main.Exception;

public class InvalidLengthException extends Exception {
    public InvalidLengthException() {
        super("Invalid contract length, contract length should to be an integer from 1 to 5.");
    }
}
