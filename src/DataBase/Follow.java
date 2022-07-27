package DataBase;

import Exceptions.DataBaseExceptions.UsernameNotExistException;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Follow {
    public static void follow(String follower_username, String following_username,
                              int followers_following_num, int followings_follower_num)
            throws SQLException {
        Statement statement = DBConnection.getInstance().getConnection().createStatement();

        // check if follower exists
        if(!Signup.isUsernameExist(follower_username))
            throw new UsernameNotExistException("follower username does not exist");

        // check if following exists
        if(!Signup.isUsernameExist(following_username))
            throw new UsernameNotExistException("following username does not exist");

        // check if not followed already
        if(doesFollow(follower_username, following_username))
            throw new SQLException("you are following user already");

        statement.executeUpdate("INSERT INTO Follows (FollowerUserName, FollowingUserName)" +
                 " VALUES ('" + follower_username + "', '" + following_username + "');");

        statement.executeUpdate("UPDATE Users SET NumberOfFollowing = " + followers_following_num +
                " WHERE Username = '" + follower_username + "';");
        statement.executeUpdate("UPDATE Users SET NumberOfFollowers = " + followings_follower_num +
                " WHERE Username = '" + following_username + "';");

        statement.close();
    }

    public static void unfollow(String follower_username, String following_username,
                                int followers_following_num, int followings_follower_num)
            throws SQLException {
        Statement statement = DBConnection.getInstance().getConnection().createStatement();

        // check if follower exists
        if(!Signup.isUsernameExist(follower_username))
            throw new UsernameNotExistException("follower username does not exist");

        // check if following exists
        if(!Signup.isUsernameExist(following_username))
            throw new UsernameNotExistException("following username does not exist");

        // check if not followed
        if(!doesFollow(follower_username, following_username))
            throw new SQLException("you are not following user");

        statement.executeUpdate("DELETE FROM Follows WHERE FollowingUserName = '" +
                following_username + "' AND FollowerUserName = '" + follower_username + "';");

        statement.executeUpdate("UPDATE Users SET NumberOfFollowing = " + followers_following_num +
                " WHERE Username = '" + follower_username + "';");
        statement.executeUpdate("UPDATE Users SET NumberOfFollowers = " + followings_follower_num +
                " WHERE Username = '" + following_username + "';");

        System.out.println(follower_username + " unfollowed " + following_username);

        statement.close();
    }

    public static ArrayList<String> followersList(String username) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        ResultSet resultset = statement.getResultSet();
        resultset = statement.executeQuery("SELECT FollowerUsername FROM Follows WHERE " +
                "FollowingUsername = '" + username + "';");

        while(resultset.next())
            result.add(resultset.getString(1));

        return result;
    }

    public static ArrayList<String> followingsList(String username) throws SQLException {
        ArrayList<String> result = new ArrayList<String>();
        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        ResultSet resultset = statement.getResultSet();
        resultset = statement.executeQuery("SELECT FollowingUsername FROM Follows WHERE " +
                "FollowerUsername = '" + username + "';");

        while(resultset.next())
            result.add(resultset.getString(1));

        return result;
    }

    public static boolean doesFollow(String follower_username, String following_username) throws SQLException {
        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        return statement.executeQuery("SELECT * FROM Follows WHERE FollowerUserName = '"+
                follower_username + "' AND FollowingUserName = '" + following_username + "' LIMIT 1;").next();
    }

}
