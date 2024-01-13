package main.Exception;

public class InvalidBonusException extends Exception {
    public InvalidBonusException() {
        super("Invalid Bonus amount, bonus should to be an integer from 0 to 75,000.");
    }
}
