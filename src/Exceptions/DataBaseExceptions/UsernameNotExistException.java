package Exceptions.DataBaseExceptions;

import java.sql.SQLException;

public class UsernameNotExistException extends SQLException {
    public UsernameNotExistException(String message) {
        super(message);
    }
}
