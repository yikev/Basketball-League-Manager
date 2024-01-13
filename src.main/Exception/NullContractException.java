package main.Exception;

public class NullContractException  extends Exception {
    public NullContractException() {
        super("No contract selected.");
    }
}
