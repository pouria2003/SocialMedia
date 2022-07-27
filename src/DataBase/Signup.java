package DataBase;

import Exceptions.DataBaseExceptions.UsernameExistException;
import BusinessLogic.User.User;

import java.sql.SQLException;
import java.sql.Statement;

public class Signup {
    public static void SignUserUp(User user, String sec_ans1, String sec_ans2, String sec_ans3) throws SQLException {
        if(user == null)
            throw new NullPointerException();

        Statement statement = DBConnection.getInstance().getConnection().createStatement();

        // check if userName exists
        if(isUsernameExist(user.getUsername()))
            throw new UsernameExistException("username already exists");

        // add user to database
        statement.executeUpdate("INSERT INTO USERS (UserName, Password, " +
                "NumberOfFollowers, NumberOfFollowing, SecurityAnswer1" +
                ", SecurityAnswer2, SecurityAnswer3) VALUES ('" +
                user.getUsername() + "', '" + user.getPassword() + "', " +
                "0, 0, '" + sec_ans1 + "', '" + sec_ans2 + "', '" + sec_ans3 + "');");
        statement.close();
    }

    public static boolean isUsernameExist(String username) throws SQLException {
        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        return statement.executeQuery("SELECT Username FROM Users WHERE Username = '" +
                username + "';").next();
    }
}
