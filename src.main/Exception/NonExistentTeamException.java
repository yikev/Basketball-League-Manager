package main.Exception;

public class NonExistentTeamException extends Exception {
    public NonExistentTeamException() {
        super("Must add player to an existing Team + City Combination.");
    }
}
