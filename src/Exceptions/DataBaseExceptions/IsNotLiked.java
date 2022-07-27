package Exceptions.DataBaseExceptions;

import java.sql.SQLException;

public class IsNotLiked extends SQLException {
    public IsNotLiked() { super(); }
    public IsNotLiked(String message) { super(message); }
}
