package Exceptions.LogicException;

public class UsernameFormatException extends IllegalStateException {
    public UsernameFormatException() { super(); }
    public UsernameFormatException(String message) { super(message); }
}
