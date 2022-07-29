package DataBase;

import BusinessLogic.Message.Message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Chat {
    public static ArrayList<String> chats(String username) throws SQLException {
        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        ResultSet resultset = statement.executeQuery("select ChatName from chats where ChatName Like '"
                + username + " %' or ChatName Like '% " + username + "';");

        ArrayList<String> result = new ArrayList<String>();

        while(resultset.next()) {
            result.add(resultset.getString("ChatName"));
        }

        resultset.close();
        statement.close();

        return result;
    }

    public static ArrayList<Message> getMessages(String chat_name) throws SQLException {
        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        ResultSet resultset = statement.executeQuery("SELECT Content, Username, id, RepliedTo " +
                "FROM Messages WHERE ChatName = '" + chat_name + "' ORDER BY id ASC;");

        ArrayList<Message> messages = new ArrayList<Message>();
        while(resultset.next()) {
            resultset.getInt("RepliedTo");
            if(resultset.wasNull()) {
                messages.add(new Message(resultset.getString("Content")
                        , resultset.getString("Username"), resultset.getInt("id")));
            }
            else {
                messages.add(new Message(resultset.getString("Content")
                        , resultset.getString("Username"), resultset.getInt("id")
                        , resultset.getInt("RepliedTo")));
            }
        }

        return messages;
    }

    public static void newMessage(String content, String username, int replied_to, String chat_name) throws SQLException {
        Statement statement = DBConnection.getInstance().getConnection().createStatement();

        ResultSet resultset = statement.executeQuery("SELECT LastMessageId FROM Chats" +
                " WHERE ChatName = '" + chat_name + "';");
        resultset.next();
        System.out.println("chat name = " + chat_name);
        int last_message_id = resultset.getInt("LastMessageId");

        if(replied_to < 0) {
            statement.executeUpdate("INSERT INTO Messages (ChatName, Username, id, Content)" +
                    " VALUES ('" + chat_name + "', '" + username + "', " +
                    (last_message_id + 1) + ", '" + content + "');");
        } else {
            statement.executeUpdate("INSERT INTO Messages (ChatName, Username, id, RepliedTo, Content)" +
                    " VALUES ('" + chat_name + "', '" + username + "', " +
                    (last_message_id + 1) + ", " + replied_to + ", '" + content + "');");
        }

        statement.executeUpdate("UPDATE Chats SET LastMessageId = " + (last_message_id + 1)
                + " WHERE ChatName = '" + chat_name + "';");

        resultset.close();
        statement.close();
    }

    public static boolean isChatExists(String chat_name) throws SQLException {
        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        ResultSet resultset = statement.executeQuery("SELECT * FROM Chats WHERE ChatName = '" + chat_name + "';");
        return resultset.next();
    }

    public static void newChat(String username1, String username2) throws SQLException {
        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        statement.executeUpdate("INSERT INTO Chats (ChatName, LastMessageId) VALUES " +
                "('" + username1 + " " + username2 + "', 0)");
    }

    public static Message getMessage(String chat_name, int id) throws SQLException {
        Statement statement = DBConnection.getInstance().getConnection().createStatement();
        ResultSet resultset = statement.executeQuery("SELECT Content, Username, id, RepliedTo " +
                "FROM Messages WHERE ChatName = '" + chat_name + "' AND Id = " + id + ";");
        resultset.next();
        resultset.getInt("RepliedTo");
        if(resultset.wasNull()) {
            return new Message(resultset.getString("Content")
                    , resultset.getString("Username")
                    , resultset.getInt("id"));
        }
        else {
            return new Message(resultset.getString("Content")
                    , resultset.getString("Username")
                    , resultset.getInt("id")
                    , resultset.getInt("RepliedTo"));
        }

    }
}
