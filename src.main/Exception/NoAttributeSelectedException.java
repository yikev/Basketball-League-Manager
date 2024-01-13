package main.Exception;

public class NoAttributeSelectedException extends Exception {
    public NoAttributeSelectedException() {
        super("No attribute has been selected.");
    }
}
