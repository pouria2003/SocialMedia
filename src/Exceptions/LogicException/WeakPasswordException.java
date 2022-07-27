package Exceptions.LogicException;

public class WeakPasswordException extends IllegalStateException {
    public WeakPasswordException() { super(); }
    public WeakPasswordException(String message) { super(message); }
}
