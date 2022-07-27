package Exceptions.LogicException;

public class PasswordLengthException extends IllegalStateException {
    public PasswordLengthException() { super(); }
    public PasswordLengthException(String message) { super(message); }
}
