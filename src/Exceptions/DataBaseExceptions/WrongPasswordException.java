package Exceptions.DataBaseExceptions;

import java.sql.SQLException;

public class WrongPasswordException extends SQLException {
    public WrongPasswordException(String message) {
        super(message);
    }
}
