package BusinessLogic.Message;

public class Message {
    private final String message;
    private final String username;
    private final int id;
    private int replied_to;

    public Message(String message, String username, int id) {
        this.message = message;
        this.username = username;
        this.id = id;
        this.replied_to = -1;
    }

    public Message(String message, String username, int id, int replied_to) {
        this(message, username, id);
        this.replied_to = replied_to;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public int getRepliedTo() {
        return replied_to;
    }
}
