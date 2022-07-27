package Exceptions.DataBaseExceptions;

import java.sql.SQLException;

public class UsernameExistException extends IllegalStateException {
    public UsernameExistException() { super(); }
    public UsernameExistException(String message){
        super(message);
    }
}
