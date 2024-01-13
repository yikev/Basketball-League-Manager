package main.Exception;

public class InvalidSalaryException extends Exception {
    public InvalidSalaryException() {
        super("Invalid salary, it needs to be a positive integer");
    }
}
