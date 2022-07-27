package Exceptions.DataBaseExceptions;

import java.sql.SQLException;

public class AlreadyLiked extends SQLException {
    public AlreadyLiked() { super(); }
    public AlreadyLiked(String message) { super(message); }
}
