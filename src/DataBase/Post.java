package DataBase;

import BusinessLogic.Post.Comment;
import Exceptions.DataBaseExceptions.AlreadyLiked;
import Exceptions.DataBaseExceptions.IsNotLiked;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.time.LocalDateTime;

public class Post {
    public static void addPost(BusinessLogic.Post.Post post, String username) throws SQLException {
        LocalDateTime now = LocalDateTime.now();

        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        statement.executeUpdate("INSERT INTO PostsAndComments (CommentContent, CommentId" +
                ", Username, RepliedTo, DatePosted) VALUES ('" + post.getContent() + "', '" + post.getId() +
                "', '" + post.getUsername() + "', null, '" + now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth() + "');");

        ResultSet resultset = statement.getResultSet();

        resultset = statement.executeQuery("SELECT NumberOfPosts, LastPostId " +
                "FROM Users WHERE Username = '" + username + "';");

        resultset.next();

        statement.executeUpdate("UPDATE Users SET NumberOfPosts = "
                + (resultset.getInt("NumberOfPosts") + 1) + ", LastPostId = "
                + (resultset.getInt("LastPostId") + 1)
                + " WHERE Username = '" + username + "';");

        resultset.close();
        statement.close();
    }

    public static ArrayList<BusinessLogic.Post.Post> postsList(String username) throws SQLException {
        ArrayList<BusinessLogic.Post.Post> posts = new ArrayList<BusinessLogic.Post.Post>();

        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        ResultSet resultset = statement.executeQuery("SELECT CommentContent, CommentId, Username, Likes, NumberOfReplies " +
                "FROM PostsAndComments WHERE Username = '" + username + "' AND RepliedTo is null;");

        while(resultset.next())
            posts.add(new BusinessLogic.Post.Post(resultset.getString("CommentContent")
                    , resultset.getString("CommentId")
                    , resultset.getString("Username")
                    , resultset.getInt("Likes")
                    , resultset.getInt("NumberOfReplies")));

        resultset.close();
        statement.close();

        return posts;
    }

    public static BusinessLogic.Post.Post post(String post_id) throws SQLException {
        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        ResultSet resultset = statement.executeQuery("SELECT CommentContent, CommentId, Username, Likes, NumberOfReplies " +
                "FROM PostsAndComments WHERE CommentId = '" + post_id + "';");

        resultset.next();

        return new BusinessLogic.Post.Post(resultset.getString("CommentContent")
                , resultset.getString("CommentId")
                , resultset.getString("Username")
                ,resultset.getInt("Likes")
                , resultset.getInt("NumberOfReplies"));
    }

    public static void like(String username, String post_id) throws SQLException {
        if(doesLike(username, post_id))
            throw new AlreadyLiked();

        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        ResultSet resultset = statement.executeQuery("SELECT Likes FROM PostsAndComments " +
                "WHERE CommentId = '" + post_id + "';");
        resultset.next();
        statement.executeUpdate("UPDATE PostsAndComments SET Likes = "
                + (resultset.getInt("Likes") + 1) +
                " WHERE CommentId = '" + post_id + "';");

        statement.executeUpdate("INSERT INTO Likes (Username, PostOrCommentId) VALUES ('"
        + username + "', '" + post_id + "');");
    }

    public static void unLike(String username, String post_id) throws SQLException {
        if(!doesLike(username, post_id))
            throw new IsNotLiked();

        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        ResultSet resultset = statement.executeQuery("SELECT Likes FROM PostsAndComments " +
                "WHERE CommentId = '" + post_id + "';");
        resultset.next();
        statement.executeUpdate("UPDATE PostsAndComments SET Likes = "
                + (resultset.getInt("Likes") - 1) +
                " WHERE CommentId = '" + post_id + "';");

        statement.executeUpdate("DELETE FROM Likes WHERE Username = '" + username
                + "' AND PostOrCommentId = '" + post_id + "';");
    }

    public static void deletePost(String post_id, String username) throws SQLException {
        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        statement.executeUpdate("DELETE FROM PostsAndComments WHERE CommentId = '" + post_id + "';");

        ResultSet resultset = statement.getResultSet();
        resultset = statement.executeQuery("SELECT NumberOfPosts " +
                "FROM Users WHERE Username = '" + username + "';");

        resultset.next();

        statement.executeUpdate("UPDATE Users SET NumberOfPosts = "
                + (resultset.getInt("NumberOfPosts") - 1)
                + " WHERE Username = '" + username + "';");
        resultset.close();
        statement.close();
    }

    public static void addComment(Comment comment) throws SQLException {
        LocalDateTime now = LocalDateTime.now();

        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        statement.executeUpdate("INSERT INTO PostsAndComments (CommentContent, CommentId, Username" +
                ", RepliedTo, DatePosted) VALUES ('" + comment.getContent() + "', '" + comment.getId() + "', '" +
                comment.getUsername() + "', '" + comment.getRepliedTo() + "', '" + now.getYear()
                + "-" + now.getMonthValue() + "-" + now.getDayOfMonth() + "');");

        ResultSet resultset = statement.executeQuery("SELECT NumberOfReplies FROM PostsAndComments " +
                "WHERE CommentId = '" + comment.getRepliedTo() + "';");
        resultset.next();
        statement.executeUpdate("UPDATE PostsAndComments SET NumberOfReplies = " +
                (resultset.getInt("NumberOfReplies") + 1) + " WHERE CommentId = '" + comment.getRepliedTo() + "';");

        statement.close();
    }

    public static ArrayList<Comment> repliesList(String replied_to_id) throws SQLException {
        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        ResultSet result_set = statement.executeQuery("SELECT CommentContent, CommentId, RepliedTo, Username" +
                ", Likes, NumberOfReplies FROM PostsAndComments WHERE RepliedTo = '" + replied_to_id + "';");

        ArrayList<Comment> comments = new ArrayList<Comment>();
        while(result_set.next()) {
            comments.add(new Comment(result_set.getString("CommentContent"),
                    result_set.getString("CommentId")
                    , result_set.getString("Username")
                    , result_set.getString("RepliedTo"), result_set.getInt("Likes")
                    , result_set.getInt("NumberOfReplies")));
        }
        return comments;
    }

    public static Comment comment(String comment_id) throws SQLException {
        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        ResultSet resultset = statement.executeQuery("SELECT CommentContent, CommentId, Username, RepliedTo, Likes, NumberOfReplies " +
                "FROM PostsAndComments WHERE CommentId = '" + comment_id + "';");

        resultset.next();

        return new Comment(resultset.getString("CommentContent")
                , resultset.getString("CommentId")
                , resultset.getString("Username")
                , resultset.getString("RepliedTo")
                , resultset.getInt("Likes")
                , resultset.getInt("NumberOfReplies"));
    }

    public static boolean doesLike(String username, String comment_id) throws SQLException {
        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        return statement.executeQuery("SELECT * FROM Likes WHERE Username = '" +
                username + "' AND PostOrCommentId = '" + comment_id + "';").next();
    }

}
