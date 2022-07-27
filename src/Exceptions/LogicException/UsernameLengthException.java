package Exceptions.LogicException;

public class UsernameLengthException extends IllegalStateException {
    public UsernameLengthException() { super(); }
    public UsernameLengthException(String message) { super(message); }
}
