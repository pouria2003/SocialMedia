package DataBase;

import Exceptions.DataBaseExceptions.UsernameNotExistException;
import Exceptions.DataBaseExceptions.WrongPasswordException;
import BusinessLogic.User.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReadUser {
    /// if password equals to null then function will not check password
    public static User readUser(String userName, String password) throws SQLException {
        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Users WHERE UserName = '" +
                userName +"';");
        // check if user exists
        if(!resultSet.next()) {
            resultSet.close();
            statement.close();
            throw new UsernameNotExistException("User does not exists");
        }
        if(password != null && !resultSet.getString("Password").equals(password)){
            resultSet.close();
            statement.close();
            throw new WrongPasswordException("password is not correct");
        }
        User user = new User(resultSet.getString("UserName"), resultSet.getString("Password"),
                resultSet.getInt("NumberOfFollowers"), resultSet.getInt("NumberOfFollowing"),
                resultSet.getInt("NumberOfPosts"), resultSet.getInt("LastPostId"));
        resultSet.close();
        statement.close();
        return user;
    }
}
