package Exceptions.LogicException;

public class PasswordFormatException extends IllegalStateException {
    public PasswordFormatException() { super(); }
    public PasswordFormatException(String message) { super(message); }
}
