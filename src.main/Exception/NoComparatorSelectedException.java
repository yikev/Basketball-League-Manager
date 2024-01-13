package main.Exception;

public class NoComparatorSelectedException extends Exception {
    public NoComparatorSelectedException() {
        super("No attribute has been selected.");
    }
}
